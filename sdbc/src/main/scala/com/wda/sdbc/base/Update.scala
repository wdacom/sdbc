package com.wda.sdbc.base

trait Update {
  self: Connection with ParameterValue with AbstractQuery =>

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
