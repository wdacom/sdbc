package com.wda.sdbc.base

trait SelectForUpdate {
  self: Connection with ParameterValue with AbstractQuery with Row with MutableRow =>

  type UnderlyingMutableResultSet

  protected implicit def MutableResultSetToMutableRowIterator(result: UnderlyingMutableResultSet): Iterator[UnderlyingMutableRow]

  trait MutablePreparer {
    def prepare(queryText: String)(implicit connection: UnderlyingConnection): PreparedStatement
  }

  val isMutablePreparer: MutablePreparer

  trait QueryUpdatable {
    def executeQuery(statement: PreparedStatement)(implicit connection: UnderlyingConnection): UnderlyingMutableResultSet
  }

  val isQueryUpdatable: QueryUpdatable

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

    def iterator()(implicit connection: UnderlyingConnection): Iterator[UnderlyingMutableRow] = {
      logger.debug(s"""Retrieving an iterator of updatable rows using "${statement.originalQueryText}" with parameters $parameterValues.""")
      isQueryUpdatable.executeQuery(isMutablePreparer.prepare(queryText))
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
