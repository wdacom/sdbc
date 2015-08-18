package com.wda.sdbc.cassandra.datastax

import com.datastax.driver.core
import com.google.common.util.concurrent.{Futures, FutureCallback, ListenableFuture}
import com.wda.sdbc.base
import com.wda.sdbc.cassandra.datastax

import scala.concurrent.{Promise, Future, ExecutionContext}

package object implementation {

  type ParameterValue[+T] = base.ParameterValue[T, core.BoundStatement, Int]

  type ParameterList = Seq[(String, Option[ParameterValue[_]])]

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, core.BoundStatement, Int]

  type Index = PartialFunction[core.Row, Int]

  type RowGetter[+T] = base.Getter[core.Row, Index, T]

  type TupleGetter[+T] = base.Getter[core.TupleValue, Int, T]

  type Session = core.Session

  type Cluster = core.Cluster

  type Executable[Key] = base.Executable[Key, Session, datastax.Execute]

  type Selectable[Key, Value] = base.Selectable[Key, Value, Session, datastax.Select[Value]]

  private [datastax] def prepare(
    select: ParameterizedQuery[_] with HasQueryOptions
  )(implicit session: Session
  ): core.BoundStatement = {
    prepare(
      select.statement,
      select.parameterValues,
      select.queryOptions
    )
  }

  private [datastax] def prepare(
    statement: base.CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]],
    queryOptions: QueryOptions
  )(implicit session: Session
  ): core.BoundStatement = {
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

  private [datastax] def toScalaFuture[T](f: ListenableFuture[T])(implicit ec: ExecutionContext): Future[T] = {
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
