package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.Logging
import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.CompiledStatement

case class Execute private [jdbc] (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[Any]]
)(implicit parameterSetter: ParameterSetter
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

  override protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[Any]]
  ): Execute = {
    Execute(statement, parameterValues)
  }
}

object Execute {
  def apply(
    queryText: String,
    hasParameters: Boolean = true
  )(implicit parameterSetter: ParameterSetter
  ): Execute = {
    Execute(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[Any]]
    )
  }
}
