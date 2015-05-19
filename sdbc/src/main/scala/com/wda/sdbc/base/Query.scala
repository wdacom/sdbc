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
trait Query[Self <: Query[Self, UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow], UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow]
  extends Logging {
  abstractQuery =>

  def closeUnderlyingQuery: Closable[UnderlyingQuery]

  def isConnection: QueryMethods[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow]


  protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]]
  ): Self


}

trait StringQuery[Self <: Query[Self, UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow], UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow]
  extends Query[Self, UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow] {
  abstractQuery =>

  protected def statement: CompiledStatement

  def parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]]

  /**
   * The query text with name parameters replaced with positional parameters.
   * @return
   */
  def queryText: String = statement.queryText

  def originalQueryText: String = statement.originalQueryText

  def parameterPositions: Map[String, Set[Int]] = statement.parameterPositions

  private def setParameter(
    parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]],
    nameValuePair: (String, Option[ParameterValue[_, UnderlyingQuery]])
  ): Map[String, Option[ParameterValue[_, UnderlyingQuery]]] = {
    if (parameterPositions.contains(nameValuePair._1)) {
      parameterValues + nameValuePair
    } else {
      throw new IllegalArgumentException(s"${nameValuePair._1} is not a parameter in the query.")
    }
  }

  def on(parameterValues: (String, Option[ParameterValue[_, UnderlyingQuery]])*): Self = {
    val newValues = setParameters(parameterValues: _*)
    subclassConstructor(statement, newValues)
  }

  protected def setParameters(nameValuePairs: (String, Option[ParameterValue[_, UnderlyingQuery]])*): Map[String, Option[ParameterValue[_, UnderlyingQuery]]] = {
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
  protected def withUnderlyingQuery[T](
    f: UnderlyingQuery => T
  )(
    implicit connection: UnderlyingConnection
  ): T = {
    val statement = isConnection.prepare(connection, queryText)
    try {
      f(statement)
    } finally {
      //Close the result set, but don't throw any errors if it's already closed.
      closeUnderlyingQuery.closeQuietly(statement)
    }
  }

  def execute()(
    implicit connection: UnderlyingConnection
  ): Unit = {
    logger.debug( s"""Executing the query "$originalQueryText" with parameters $parameterValues.""")
    withUnderlyingQuery[Unit](statement => isConnection.execute(statement))
  }
  
}
