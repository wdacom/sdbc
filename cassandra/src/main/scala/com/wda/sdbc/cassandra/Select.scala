package com.wda.sdbc.cassandra

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
  queryOptions: QueryOptions
)(implicit val converter: CRow => T)
  extends base.Select[Session, T]
  with base.ParameterizedQuery[Select[T], BoundStatement, Int]
  with Logging {

  def iterator()(implicit connection: Session): Iterator[T] = {
    logger.debug(s"""Retrieving an iterator using "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(statement, parameterValues, queryOptions)
    connection.execute(prepared).iterator.asScala.map(converter)
  }

  def iteratorAsync()(implicit connection: Session, ec: ExecutionContext): Future[Iterator[T]] = {
    logger.debug(s"""Retrieving an iterator asynchronously using "$originalQueryText" with parameters $parameterValues.""")

    val prepared = prepare(statement, parameterValues, queryOptions)
    val toListen = connection.executeAsync(prepared)

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
    logger.debug(s"""Executing "$originalQueryText" with parameters $parameterValues.""")

    val prepared = prepare(statement, parameterValues, queryOptions)
    connection.execute(prepared)
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
    queryOptions: QueryOptions = QueryOptions.default
  )(implicit converter: CRow => T
  ): Select[T] = {
    Select[T](
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]],
      queryOptions
    )
  }
}
