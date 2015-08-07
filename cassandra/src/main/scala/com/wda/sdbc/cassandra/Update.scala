package com.wda.sdbc.cassandra

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement
import com.datastax.driver.core._

case class Update private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]],
  queryOptions: QueryOptions
) extends base.Update[Session]
  with base.ParameterizedQuery[Update, BoundStatement, Int]
  with Logging {

  /**
   * Cassandra doesn't report the number of rows that were updated, so
   * update() just calls execute() and returns a dummy value.
   * @param connection
   * @return -1, since Cassandra doesn't report the number of updated rows.
   */
  override def update()(implicit connection: Session): Long = {
    execute()
    -1
  }

  def execute()(implicit connection: Session): Unit = {
    logger.debug(s"""Executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(statement, parameterValues, queryOptions)
    connection.execute(prepared)
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Update = {
    copy(
      statement = statement,
      parameterValues = parameterValues
    )
  }
}

object Update {
  def apply(
    queryText: String,
    hasParameters: Boolean = true,
    queryOptions: QueryOptions = QueryOptions.default
  ): Update = {
    Update(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]],
      queryOptions
    )
  }
}
