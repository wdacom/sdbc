package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.Logging
import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.CompiledStatement

case class Update private [jdbc] (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]]
) extends base.Update[Connection]
  with ParameterizedQuery[Update]
  with Logging {

  override def update()(implicit connection: Connection): Long = {
    logger.debug(s"""Executing "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(
      queryText = queryText,
      parameterValues = parameterValues,
      parameterPositions = parameterPositions
    )

    val result = prepared.executeUpdate()
    prepared.close()
    result
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Update = {
    Update(statement, parameterValues)
  }
}

object Update {
  def apply(
    queryText: String,
    hasParameters: Boolean = true
  ): Update = {
    Update(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]]
    )
  }
}
