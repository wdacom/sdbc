package com.wda.sdbc.base

import scala.language.reflectiveCalls

trait Update {
  self: Connection with ParameterValue with AbstractQuery =>

  protected trait Updateable {
    def executeUpdate(statement: PreparedStatement): Int

    def executeLargeUpdate(statement: PreparedStatement): Long
  }

  protected val isUpdatable: Updateable

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

    def update()(implicit connection: UnderlyingConnection): Int = {
      logger.debug(s"""Executing an update using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement(statement => isUpdatable.executeUpdate(statement))
    }

    def largeUpdate()(implicit connection: UnderlyingConnection): Long = {
      logger.debug(s"""Executing a large update using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement(statement => isUpdatable.executeLargeUpdate(statement))
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
