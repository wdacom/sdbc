package com.wda.sdbc.jdbc

import java.sql.Connection

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement

case class Execute private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]]
) extends base.Execute[Connection]
  with ParameterizedQuery[Execute]
  with Logging {

  override def execute()(implicit connection: Connection): Unit = {
    logger.debug(s"""Executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(
      queryText = queryText,
      parameterValues = parameterValues,
      parameterPositions = parameterPositions
    )

    prepared.execute()
    prepared.close()
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Execute = {
    Execute(statement, parameterValues)
  }
}

object Execute {
  def apply(
    queryText: String,
    hasParameters: Boolean = true
  ): Execute = {
    Execute(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]]
    )
  }
}
