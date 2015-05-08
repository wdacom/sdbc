package com.wda.sdbc.base

import scala.collection.immutable.Seq
import scala.language.reflectiveCalls

trait Select[
  QueryResult,
  WrappedConnection <: {def close(): Unit},
  PreparedStatement <: {def close(): Unit; def execute(): Unit; def setNull(parameterIndex: Int): Unit; def executeQuery(): QueryResult},
  WrappedRow
] {
  self: Connection[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with ParameterValue[WrappedRow, PreparedStatement] with AbstractQuery[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with Row[WrappedRow, PreparedStatement] =>

  implicit def QueryResultToRowIterator(result: QueryResult): Iterator[Row]

  case class Select[T] private[sdbc] (
    statement: CompiledStatement,
    override val parameterValues: Map[String, Option[ParameterValue[_]]]
  )(implicit converter: Row => T
  ) extends AbstractQuery[Select[T]] {

    override protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_]]]
    ): Select[T] = {
      Select[T](statement, parameterValues)
    }

    def iterator()(implicit connection: Connection): Iterator[T] = {
      logger.debug(s"""Retrieving an Iterator using "${statement.originalQueryText}" with parameters $parameterValues.""")
      connection.prepareStatement(queryText).executeQuery().map(row => converter(row))
    }

    def seq()(implicit connection: Connection): Seq[T] = {
      logger.debug(s"""Retrieving a Seq using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Vector[T]](_.executeQuery().map(converter).toVector)
    }

    def option()(implicit connection: Connection): Option[T] = {
      logger.debug(s"""Retrieving an Option using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Option[T]](_.executeQuery().map(converter).toStream.headOption)
    }

    def single()(implicit connection: Connection): T = {
      logger.debug(s"""Retrieving a single row using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[T](_.executeQuery().map(converter).toStream.head)
    }

  }

  object Select {

    def apply[T](
      queryText: String,
      hasParameters: Boolean = true
    )(implicit converter: Row => T
    ): Select[T] = {
      val statement = CompiledStatement(queryText, hasParameters)
      Select[T](statement, Map.empty[String, Option[ParameterValue[_]]])
    }

  }

}
