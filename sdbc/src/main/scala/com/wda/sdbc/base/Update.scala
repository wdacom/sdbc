package com.wda.sdbc.base

import scala.language.reflectiveCalls

trait Update[
  QueryResult,
  WrappedConnection <: {def close(): Unit},
  PreparedStatement <: {def close(): Unit; def execute(): Unit; def setNull(parameterIndex: Int): Unit; def executeUpdate(): Int; def executeLargeUpdate(): Long},
  WrappedRow
] {
  self: Connection[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with ParameterValue[WrappedRow, PreparedStatement] with AbstractQuery[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] =>

  case class Update private[sdbc] (
    statement: CompiledStatement,
    override val parameterValues: Map[String, Option[ParameterValue[_]]]
  ) extends AbstractQuery[Update] {

    override protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_]]]
    ): Update = {
      Update(statement, parameterValues)
    }

    def update()(implicit connection: Connection): Int = {
      logger.debug(s"""Executing an update using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement(_.executeUpdate())(connection)
    }

    def largeUpdate()(implicit connection: Connection): Long = {
      logger.debug(s"""Executing a large update using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement(_.executeLargeUpdate())(connection)
    }

  }

  object Update {

    def apply(
      queryText: String,
      hasParameters: Boolean = true
    ): Update = {
      val statement = CompiledStatement(queryText, hasParameters)
      Update(statement, Map.empty[String, Option[ParameterValue[_]]])
    }

  }

}
