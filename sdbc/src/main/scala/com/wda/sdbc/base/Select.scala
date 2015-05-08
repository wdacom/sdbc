package com.wda.sdbc.base

import scala.collection.immutable.Seq
import scala.language.reflectiveCalls

trait Select {
  self: Connection with ParameterValue with AbstractQuery with Row =>

  type ResultSet

  implicit def QueryResultToRowIterator(result: ResultSet): Iterator[Row]

  trait Queryable {
    def executeQuery(statement: PreparedStatement)(implicit connection: Connection): ResultSet
  }

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

    def iterator()(implicit connection: Connection, ev0: Preparer, ev1: Queryable): Iterator[T] = {
      logger.debug(s"""Retrieving an Iterator using "${statement.originalQueryText}" with parameters $parameterValues.""")
      ev1.executeQuery(ev0.prepare(connection, queryText)).map(converter)
    }

    def seq()(implicit connection: Connection, ev0: Preparer, ev1: Closable[PreparedStatement], ev2: Queryable): Seq[T] = {
      logger.debug(s"""Retrieving a Seq using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Vector[T]](statement => ev2.executeQuery(statement).map(converter).toVector)
    }

    def option()(implicit connection: Connection, ev0: Preparer, ev1: Closable[PreparedStatement], ev2: Queryable): Option[T] = {
      logger.debug(s"""Retrieving an Option using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Option[T]](statement => ev2.executeQuery(statement).map(converter).toStream.headOption)
    }

    def single()(implicit connection: Connection, ev0: Preparer, ev1: Closable[PreparedStatement], ev2: Queryable): T = {
      logger.debug(s"""Retrieving a single row using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[T](statement => ev2.executeQuery(statement).map(converter).toStream.head)
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
