package com.wda.sdbc.base

import com.wda.Logging

/**
 * Given a query with named parameters beginning with '$',
 * construct the query for use with JDBC, so that named
 * parameters are replaced by '?', and each parameter
 * has a map to its positions in the query.
 *
 * Identifiers must start with a letter or underscore, and then
 * any character after the first one can be a letter, number,
 * underscore, or '\$'. An identifier that does not follow
 * this scheme must be quoted by backticks.
 *
 * Examples of identifiers:
 * \$hello
 * \$`hello there`
 * \$_i_am_busy
 */
trait AbstractQuery[Self <: AbstractQuery[Self, UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow], UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow]
  extends Logging {
  abstractQuery =>

  def closePreparedStatement: Closable[PreparedStatement]

  def isConnection: Connection[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow]

  protected def statement: CompiledStatement

  def parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]]

  protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]]
  ): Self

  /**
   * The query text with name parameters replaced with positional parameters.
   * @return
   */
  def queryText: String = statement.queryText

  def originalQueryText: String = statement.originalQueryText

  def parameterPositions: Map[String, Set[Int]] = statement.parameterPositions

  private def setParameter(
    parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]],
    nameValuePair: (String, Option[ParameterValue[_, PreparedStatement]])
  ): Map[String, Option[ParameterValue[_, PreparedStatement]]] = {
    if (parameterPositions.contains(nameValuePair._1)) {
      parameterValues + nameValuePair
    } else {
      throw new IllegalArgumentException(s"${nameValuePair._1} is not a parameter in the query.")
    }
  }

  def on(parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*): Self = {
    val newValues = setParameters(parameterValues: _*)
    subclassConstructor(statement, newValues)
  }

  protected def setParameters(nameValuePairs: (String, Option[ParameterValue[_, PreparedStatement]])*): Map[String, Option[ParameterValue[_, PreparedStatement]]] = {
    nameValuePairs.foldLeft(parameterValues)(setParameter)
  }

  /**
   * Perform some action on a Prepared Statement, being sure to close it.
   * Do not use this method when the result depends on an open result, such as if f returned an iterator.
   * @param f
   * @param connection
   * @tparam T
   * @return
   */
  protected def withPreparedStatement[T](
    f: PreparedStatement => T
  )(
    implicit connection: UnderlyingConnection
  ): T = {
    val statement = isConnection.prepare(connection, queryText)
    try {
      f(statement)
    } finally {
      //Close the result set, but don't throw any errors if it's already closed.
      closePreparedStatement.closeQuietly(statement)
    }
  }

  def execute()(
    implicit connection: UnderlyingConnection
  ): Unit = {
    logger.debug( s"""Executing the query "$originalQueryText" with parameters $parameterValues.""")
    withPreparedStatement[Unit](statement => isConnection.execute(statement))
  }

}
