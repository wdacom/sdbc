package com.wda.sdbc.cassandra.datastax

import com.datastax.driver.core
import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement
import com.datastax.driver.core._
import com.wda.sdbc.cassandra.datastax

import scala.concurrent.{ExecutionContext, Future}

case class Execute private (
  override val statement: CompiledStatement,
  override val parameterValues: Map[String, Option[ParameterValue[_]]],
  override val queryOptions: QueryOptions
) extends base.Execute[Session]
  with ParameterizedQuery[Execute]
  with HasQueryOptions
  with Logging {

  override def execute()(implicit session: Session): Unit = {
    logger.debug(s"""Executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = datastax.prepare(statement, parameterValues, queryOptions)
    session.execute(prepared)
  }

  def executeAsync()(implicit session: Session, ec: ExecutionContext): Future[Unit] = {
    logger.debug(s"""Asynchronously executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = datastax.prepare(statement, parameterValues, queryOptions)

    toScalaFuture[ResultSet](session.executeAsync(prepared)).map(Function.const(()))
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
    queryOptions: QueryOptions = datastax.QueryOptions.default
  ): Execute = {
    Execute(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]],
      queryOptions
    )
  }
}