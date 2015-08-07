package com.wda.sdbc

import com.datastax.driver.core.{Row => CRow, ResultSet, Session, TupleValue, BoundStatement}
import com.google.common.util.concurrent.{Futures, FutureCallback, ListenableFuture}
import com.wda.sdbc.base.CompiledStatement

import scala.concurrent.{Promise, Future, ExecutionContext}

package object cassandra {

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, BoundStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, BoundStatement, Int]

  type Index = PartialFunction[CRow, Int]

  type RowGetter[+T] = base.Getter[CRow, Index, T]

  type TupleGetter[+T] = base.Getter[TupleValue, Int, T]

  private [cassandra] def prepare(
    select: Select[_]
  )(implicit session: Session
  ): BoundStatement = {
    prepare(
      select.statement,
      select.parameterValues,
      select.queryOptions
    )
  }

  private [cassandra] def prepare(
    select: Execute
  )(implicit session: Session
  ): BoundStatement = {
    prepare(
      select.statement,
      select.parameterValues,
      select.queryOptions
    )
  }

  private [cassandra] def prepare(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]],
    queryOptions: QueryOptions
  )(implicit session: Session
  ): BoundStatement = {
    val prepared = session.prepare(statement.queryText)

    val forBinding = prepared.bind()

    for ((key, maybeValue) <- parameterValues) {
      val parameterIndices = statement.parameterPositions(key)

      maybeValue match {
        case None =>
          for (parameterIndex <- parameterIndices) {
            forBinding.setToNull(parameterIndex - 1)
          }
        case Some(value) =>
          for (parameterIndex <- parameterIndices) {
            value.set(forBinding, parameterIndex - 1)
          }
      }
    }
    forBinding.setConsistencyLevel(queryOptions.consistencyLevel)
    forBinding.setSerialConsistencyLevel(queryOptions.serialConsistencyLevel)
    queryOptions.defaultTimestamp.map(forBinding.setDefaultTimestamp)
    forBinding.setFetchSize(queryOptions.fetchSize)
    forBinding.setIdempotent(queryOptions.idempotent)
    forBinding.setRetryPolicy(queryOptions.retryPolicy)

    if (queryOptions.tracing) {
      forBinding.enableTracing()
    } else {
      forBinding.disableTracing()
    }

    forBinding
  }

  private [cassandra] def toScalaFuture[T](f: ListenableFuture[T])(implicit ec: ExecutionContext): Future[T] = {
    //Thanks http://stackoverflow.com/questions/18026601/listenablefuture-to-scala-future
    val p = Promise[T]()

    val pCallback = new FutureCallback[T] {
      override def onFailure(t: Throwable): Unit = {
        p.failure(t)
      }

      override def onSuccess(result: T): Unit = {
        p.success(result)
      }
    }

    Futures.addCallback(f, pCallback)

    p.future
  }

}
