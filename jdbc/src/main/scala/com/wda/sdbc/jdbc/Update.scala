package com.wda.sdbc.jdbc

import java.sql.{PreparedStatement, Connection}

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement

case class Update private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]]
)
  extends base.Updatable[Connection]
  with ParameterizedQuery[Update]
  with Logging {

  override def update()(implicit connection: Connection): Long = {
    logger.debug(s"""Executing update "$originalQueryText" with parameter values $parameterValues.""")
    val prepared = connection.prepareStatement(queryText)
    val result = prepared.executeLargeUpdate()
    util.Try(prepared.close())
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
  def apply[T](
    queryText: String,
    hasParameters: Boolean = true
  )(implicit converter: Row => T
  ): Update = {
    Update(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]]
    )
  }
}
