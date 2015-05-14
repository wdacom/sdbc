package com.wda.sdbc.base

trait SelectForUpdate[UnderlyingConnection, PreparedStatement, MutableResultSet, UnderlyingMutableRow] {
  outer =>

  def prepare(queryText: String)(implicit connection: UnderlyingConnection): PreparedStatement

  def executeQuery(statement: PreparedStatement)(implicit connection: Connection): MutableResultSet

  protected implicit def MutableResultSetToMutableRowIterator(result: MutableResultSet): Iterator[MutableRow]

  def closePreparedStatement: Closable[PreparedStatement]

  def isConnection: Connection[UnderlyingConnection, PreparedStatement, MutableResultSet, UnderlyingMutableRow]

  case class SelectForUpdate private[sdbc](
    statement: CompiledStatement,
    override val parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]]
  ) extends AbstractQuery[SelectForUpdate, UnderlyingConnection, PreparedStatement, MutableResultSet, UnderlyingMutableRow] {

    override def isConnection: Connection[UnderlyingConnection, PreparedStatement, MutableResultSet, UnderlyingMutableRow] =
      outer.isConnection

    override def closePreparedStatement: Closable[PreparedStatement] =
      outer.closePreparedStatement

    override protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]]
    ): SelectForUpdate = {
      SelectForUpdate(statement, parameterValues)
    }

    def iterator()(implicit connection: UnderlyingConnection): Iterator[MutableRow] = {
      logger.debug( s"""Retrieving an iterator of updatable rows using "${statement.originalQueryText}" with parameters $parameterValues.""")
      executeQuery(prepare(queryText))
    }

  }

  object SelectForUpdate {

    def apply(
      queryText: String,
      hasParameters: Boolean = true
    ): SelectForUpdate = {
      val statement = CompiledStatement(queryText, hasParameters)
      SelectForUpdate(statement, Map.empty[String, Option[ParameterValue[_, PreparedStatement]]])
    }

  }

}
