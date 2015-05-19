package com.wda.sdbc.base

protected trait Updateable[PreparedStatement] {
  def executeUpdate(statement: PreparedStatement): Long
}

case class Update[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] private[sdbc] (
  statement: CompiledStatement,
  override val parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]]
)(implicit isUpdatable: Updateable[PreparedStatement],
  override val isConnection: QueryMethods[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow],
  override val closePreparedStatement: Closable[PreparedStatement]
) extends Query[Update[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow], UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] {

  override protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]]
  ): Update[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
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

  def apply[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow](
    queryText: String,
    hasParameters: Boolean = true
  )(implicit isUpdatable: Updateable[PreparedStatement],
    isConnection: QueryMethods[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow],
    closableStatement: Closable[PreparedStatement]
  ): Update[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
    val statement = CompiledStatement(queryText, hasParameters)
    Update[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow](statement, Map.empty[String, Option[ParameterValue[_, PreparedStatement]]])
  }

}
