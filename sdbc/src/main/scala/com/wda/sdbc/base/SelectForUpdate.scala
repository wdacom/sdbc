package com.wda.sdbc.base

trait SelectForUpdate {
  self: Connection with ParameterValue with AbstractQuery with Row =>

  type MutableResultSet

  type MutableRow

  implicit def QueryResultToRowIterator(result: MutableResultSet): Iterator[MutableRow]

  trait MutablePreparer {
    def prepare(queryText: String)(implicit connection: Connection): PreparedStatement
  }

  implicit def MutableRowToRow(mutableRow: MutableRow): Row

  trait QueryUpdatable {
    def executeQuery(statement: PreparedStatement)(implicit connection: Connection): MutableResultSet
  }

  case class SelectForUpdate private[sdbc] (
    statement: CompiledStatement,
    override val parameterValues: Map[String, Option[ParameterValue[_]]]
  ) extends AbstractQuery[SelectForUpdate] {

    override protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_]]]
    ): SelectForUpdate = {
      SelectForUpdate(statement, parameterValues)
    }

    def iterator()(implicit connection: Connection, ev0: MutablePreparer, ev1: QueryUpdatable): Iterator[MutableRow] = {
      logger.debug(s"""Retrieving an iterator of updatable rows using "${statement.originalQueryText}" with parameters $parameterValues.""")
      ev1.executeQuery(ev0.prepare(queryText))
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
