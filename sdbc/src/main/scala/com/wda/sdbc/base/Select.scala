package com.wda.sdbc.base

import scala.collection.immutable.Seq
import scala.language.reflectiveCalls

trait Select {
  self: Connection with ParameterValue with AbstractQuery with Row =>

  protected type ResultSet

  protected implicit def QueryResultToRowIterator(result: ResultSet): Iterator[Row]

  protected trait Queryable {
    def executeQuery(statement: PreparedStatement)(implicit connection: UnderlyingConnection): ResultSet
  }

  protected val isQueryable: Queryable

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

    def iterator()(implicit connection: UnderlyingConnection): Iterator[T] = {
      logger.debug(s"""Retrieving an Iterator using "${statement.originalQueryText}" with parameters $parameterValues.""")
      isQueryable.executeQuery(isConnection.prepare(connection, queryText)).map(converter)
    }

    def seq()(implicit connection: UnderlyingConnection): Seq[T] = {
      logger.debug(s"""Retrieving a Seq using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Vector[T]](statement => isQueryable.executeQuery(statement).map(converter).toVector)
    }

    def option()(implicit connection: UnderlyingConnection): Option[T] = {
      logger.debug(s"""Retrieving an Option using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Option[T]](statement => isQueryable.executeQuery(statement).map(converter).toStream.headOption)
    }

    def single()(implicit connection: UnderlyingConnection): T = {
      logger.debug(s"""Retrieving a single row using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[T](statement => isQueryable.executeQuery(statement).map(converter).toStream.head)
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
