package com.wda.sdbc.base

import scala.collection.immutable.Seq

trait Select[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow] {
  outer =>

  def executeQuery(query: UnderlyingQuery)(implicit connection: UnderlyingConnection): UnderlyingResultSet

  def isConnection: QueryMethods[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow]

  /*
  This design for Select is so that it only takes one type parameter.
  The DBMS implementor has to provide DBMS-specific implementation
  details before Select even becomes available for use by clients.
   */
  case class Select[T] private[sdbc] (
    statement: CompiledStatement,
    override val parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]]
  )(implicit converter: Row[UnderlyingRow] => T
  ) extends StringQuery[Select[T], UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow] {

    override def isConnection: QueryMethods[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow] =
      outer.isConnection

    override protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]]
    ): Select[T] = {
      Select[T](statement, parameterValues)
    }

    def iterator()(implicit connection: UnderlyingConnection): Iterator[T] = {
      logger.debug(s"""Retrieving an Iterator using "${statement.originalQueryText}" with parameters $parameterValues.""")
      executeQuery(isConnection.prepare(connection, queryText)).map(converter)
    }

    def seq()(implicit connection: UnderlyingConnection): Seq[T] = {
      logger.debug(s"""Retrieving a Seq using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Vector[T]](statement => executeQuery(statement).map(converter).toVector)
    }

    def option()(implicit connection: UnderlyingConnection): Option[T] = {
      logger.debug(s"""Retrieving an Option using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Option[T]](statement => executeQuery(statement).map(converter).toStream.headOption)
    }

    def single()(implicit connection: UnderlyingConnection): T = {
      logger.debug(s"""Retrieving a single row using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[T](statement => executeQuery(statement).map(converter).toStream.head)
    }

  }

  object Select {

    def apply[T](
      queryText: String,
      hasParameters: Boolean = true
    )(implicit converter: Row[UnderlyingRow] => T
    ): Select[T] = {
      val statement = CompiledStatement(queryText, hasParameters)
      Select[T](statement, Map.empty[String, Option[ParameterValue[_]]])
    }

  }

}
