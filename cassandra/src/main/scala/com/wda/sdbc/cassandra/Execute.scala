package com.wda.sdbc.cassandra

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement
import com.datastax.driver.core._

import scala.concurrent.{ExecutionContext, Future}

case class Execute private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]],
  queryOptions: QueryOptions
) extends base.Execute[Session]
  with base.ParameterizedQuery[Execute, BoundStatement, Int]
  with Logging {

  override def execute()(implicit connection: Session): Unit = {
    logger.debug(s"""Executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(statement, parameterValues, queryOptions)
    connection.execute(prepared)
  }

  def executeAsync()(implicit connection: Session, ec: ExecutionContext): Future[Unit] = {
    logger.debug(s"""Asynchronously executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(statement, parameterValues, queryOptions)

    toScalaFuture[ResultSet](connection.executeAsync(prepared)).map(Function.const(()))
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Execute = {
    copy(
      statement = statement,
      parameterValues = parameterValues
    )
  }
}

object Execute {
  def apply(
    queryText: String,
    hasParameters: Boolean = true,
    queryOptions: QueryOptions = QueryOptions.default
  ): Execute = {
    Execute(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]],
      queryOptions
    )
  }
}
