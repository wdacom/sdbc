package com.wda.sdbc.base

trait SelectForUpdate {
  self: Connection with ParameterValue with AbstractQuery with Row =>

  case class SelectForUpdate private[sdbc] (
    override protected val statement: CompiledStatement,
    override val parameterValues: Map[String, Option[ParameterValue[_]]]
  ) extends AbstractQuery[SelectForUpdate] {

    override protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_]]]
    ): SelectForUpdate = {
      SelectForUpdate(statement, parameterValues)
    }

    def iterator()(implicit connection: Connection): Iterator[MutableRow] = {
      logger.debug(s"""Retrieving an iterator of updatable rows using "${statement.originalQueryText}" with parameters $parameterValues.""")
      prepare(updatable = true).executeQuery().mutableIterator()
    }

  }

  object SelectForUpdate {

    def apply[T](
      queryText: String,
      hasParameters: Boolean = true
    ): SelectForUpdate = {
      val statement = CompiledStatement(queryText, hasParameters)
      SelectForUpdate(statement, Map.empty[String, Option[ParameterValue[_]]])
    }

  }

}
