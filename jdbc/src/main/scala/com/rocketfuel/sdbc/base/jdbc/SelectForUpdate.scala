package com.rocketfuel.sdbc.base.jdbc

import java.sql._

import com.rocketfuel.Logging
import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.CompiledStatement

case class SelectForUpdate private[jdbc] (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[Any]]
)(implicit parameterSetter: ParameterSetter
) extends base.Select[Connection, UpdatableRow]
  with ParameterizedQuery[SelectForUpdate]
  with Logging {

  private def executeQuery()(implicit connection: Connection): ResultSet = {
    logger.debug(s"""Selecting for update "$originalQueryText" with parameters $parameterValues.""")
    val preparedStatement = connection.prepareStatement(queryText, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
    bind(preparedStatement, parameterValues, parameterPositions)

    preparedStatement.executeQuery()
  }

  override def iterator()(implicit connection: Connection): Iterator[UpdatableRow] = {
    executeQuery().updatableIterator()
  }

  override def option()(implicit connection: Connection): Option[UpdatableRow] = {
    executeQuery().updatableIterator().toStream.headOption
  }

  override protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[Any]]
  ): SelectForUpdate = {
    SelectForUpdate(
      statement,
      parameterValues
    )
  }
}

object SelectForUpdate {
  def apply(
    queryText: String,
    hasParameters: Boolean = true
  )(implicit parameterSetter: ParameterSetter
  ): SelectForUpdate = {
    SelectForUpdate(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[Any]]
    )
  }
}
