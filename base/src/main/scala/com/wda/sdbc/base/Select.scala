package com.wda.sdbc.base

import scala.collection.immutable.Seq

trait Select {
  self: Connection with ParameterValue with AbstractQuery with Row =>

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
      logger.debug(s"""Retrieving an iterator using "${statement.originalQueryText}" with parameters $parameterValues.""")
      prepare().executeQuery().iterator().map(row => converter(row))
    }

    def seq()(implicit connection: Connection): Seq[T] = {
      logger.debug(s"""Retrieving a seq using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Vector[T]](_.executeQuery().iterator().map(converter).toVector)
    }

    def option()(implicit connection: Connection): Option[T] = {
      logger.debug(s"""Retrieving an option using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[Option[T]](_.executeQuery().iterator().map(converter).toStream.headOption)
    }

    def single()(implicit connection: Connection): T = {
      logger.debug(s"""Retrieving a single row using "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement[T](_.executeQuery().iterator().map(converter).toStream.head)
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
