package com.wda.sdbc.cassandra

import com.datastax.driver.core.policies.{DefaultRetryPolicy, RetryPolicy}
import com.google.common.util.concurrent.{FutureCallback, Futures}
import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement
import com.datastax.driver.core.{Row => CRow, _}
import scala.concurrent._
import scala.collection.convert.decorateAsScala._

case class Select[T] private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]],
  consistencyLevel: ConsistencyLevel,
  serialConsistencyLevel: ConsistencyLevel,
  defaultTimestamp: Option[Long],
  fetchSize: Int,
  idempotent: Boolean,
  retryPolicy: RetryPolicy,
  tracing: Boolean
)(implicit val converter: CRow => T)
  extends base.Select[T, Session]
  with base.ParameterizedQuery[Select[T], BoundStatement, Int]
  with Logging {

  private def prepareQuery()(implicit session: Session): BoundStatement = {
    val prepared = session.prepare(statement.queryText)

    val forBinding = prepared.bind()

    for ((key, maybeValue) <- parameterValues) {
      val parameterIndices = statement.parameterPositions(key)

      maybeValue match {
        case None =>
          for (parameterIndex <- parameterIndices) {
            forBinding.setToNull(parameterIndex)
          }
        case Some(value) =>
          for (parameterIndex <- parameterIndices) {
            value.set(forBinding, parameterIndex)
          }
      }
    }
    forBinding.setConsistencyLevel(consistencyLevel)
    forBinding.setSerialConsistencyLevel(serialConsistencyLevel)
    defaultTimestamp.map(forBinding.setDefaultTimestamp)
    forBinding.setFetchSize(fetchSize)
    forBinding.setIdempotent(idempotent)
    forBinding.setRetryPolicy(retryPolicy)

    if (tracing) {
      forBinding.enableTracing()
    } else {
      forBinding.disableTracing()
    }

    forBinding
  }

  override def iterator()(implicit connection: Session): Iterator[T] = {
    connection.execute(prepareQuery()).iterator.asScala.map(converter)
  }

  def iteratorAsync()(implicit connection: Session, ec: ExecutionContext): Future[Iterator[T]] = {
    val toListen = connection.executeAsync(prepareQuery())

    //Thanks http://stackoverflow.com/questions/18026601/listenablefuture-to-scala-future
    val p = Promise[ResultSet]()

    val pCallback = new FutureCallback[ResultSet] {
      override def onFailure(t: Throwable): Unit = {
        p.failure(t)
      }

      override def onSuccess(result: ResultSet): Unit = {
        p.success(result)
      }
    }

    Futures.addCallback(toListen, pCallback)

    for {
      result <- p.future
    } yield {
      result.iterator().asScala.map(converter)
    }
  }

  override def execute()(implicit connection: Session): Unit = {
    connection.execute(prepareQuery())
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Select[T] = {
    copy(
      statement = statement,
      parameterValues = parameterValues
    )
  }
}

object Select {
  def apply[T](
    queryText: String,
    hasParameters: Boolean = true,
    consistencyLevel: ConsistencyLevel = QueryOptions.DEFAULT_CONSISTENCY_LEVEL,
    serialConsistencyLevel: ConsistencyLevel = QueryOptions.DEFAULT_SERIAL_CONSISTENCY_LEVEL,
    defaultTimestamp: Option[Long] = None,
    fetchSize: Int = QueryOptions.DEFAULT_FETCH_SIZE,
    idempotent: Boolean = QueryOptions.DEFAULT_IDEMPOTENCE,
    retryPolicy: RetryPolicy = DefaultRetryPolicy.INSTANCE,
    tracing: Boolean = false
  )(implicit converter: CRow => T
  ): Select[T] = {
    Select[T](
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]],
      consistencyLevel,
      serialConsistencyLevel,
      defaultTimestamp,
      fetchSize,
      idempotent,
      retryPolicy,
      tracing
    )
  }
}
