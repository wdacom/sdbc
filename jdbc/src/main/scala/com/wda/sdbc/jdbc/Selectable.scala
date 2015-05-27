package com.wda.sdbc.jdbc

import java.sql._

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement

case class Selectable[T] private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]]
)(implicit converter: Row => T)
  extends base.Selectable[T, Connection]
  with ParameterizedQuery[Selectable[T]]
  with ResultSetImplicits
  with Logging {

  override def iterator()(implicit connection: Connection): Iterator[T] = {
    logger.debug(s"""Retrieving an iterator using "$originalQueryText" with parameters $parameterValues.""")
    val preparedStatement = connection.prepareStatement(queryText)
    preparedStatement.executeQuery().iterator().map(converter)
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Selectable[T] = {
    Selectable[T](
      statement,
      parameterValues
    )
  }
}

object Selectable {
  def apply[T](
    queryText: String,
    hasParameters: Boolean = true
  )(implicit converter: Row => T
  ): Selectable[T] = {
    Selectable[T](
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]]
    )
  }
}
