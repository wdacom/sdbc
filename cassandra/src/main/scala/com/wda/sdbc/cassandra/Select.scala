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
    logger.debug(s"""Asynchronously retrieving an iterator asynchronously using "$originalQueryText" with parameters $parameterValues.""")

    val prepared = prepare(statement, parameterValues, queryOptions)
    val toListen = connection.executeAsync(prepared)

    for {
      result <- toScalaFuture(toListen)
    } yield {
      result.iterator().asScala.map(converter)
    }
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
