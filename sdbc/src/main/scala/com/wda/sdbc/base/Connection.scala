package com.wda.sdbc.base

import scala.collection.immutable.Seq
import scala.language.reflectiveCalls

trait Connection {
  self: AbstractQuery with Row with ParameterValue with Select with Update =>

  type WrappedConnection

  type PreparedStatement

  trait Closable[T] {
    def close(connection: T): Unit

    def closeQuietly(connection: T): Unit = {
      util.Try(close(connection))
    }
  }

  trait Preparer {
    def prepare(connection: Connection, queryText: String): PreparedStatement
  }

  abstract class Connection(
    val underlying: WrappedConnection
  )(ev: Closable[WrappedConnection]) {

    def prepareStatement(query: String): PreparedStatement

    def iterator[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit converter: Row => T,
      ev0: Preparer,
      ev1: Queryable
    ): Iterator[T] = {
      implicit val connection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).iterator()
    }

    def seq[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(
      implicit converter: Row => T,
      ev0: Preparer,
      ev1: Queryable,
      ev2: Closable[PreparedStatement]
    ): Seq[T] = {
      implicit val connection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).seq()
    }

    def option[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(
      implicit converter: Row => T,
      ev0: Preparer,
      ev1: Queryable,
      ev2: Closable[PreparedStatement]
    ): Option[T] = {
      implicit val connection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).option()
    }

    def single[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(
      implicit converter: Row => T,
      ev0: Preparer,
      ev1: Queryable,
      ev2: Closable[PreparedStatement]
    ): T = {
      implicit val connection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).single()
    }

    def update(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit ev0: Preparer,
      ev1: Updateable,
      ev2: Closable[PreparedStatement]
    ): Int = {
      implicit val connection = this
      Update(queryText).on(
        parameterValues: _*
      ).update()
    }

    def largeUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit ev0: Preparer,
      ev1: Updateable,
      ev2: Closable[PreparedStatement]
    ): Long = {
      implicit val connection = this
      Update(queryText).on(
        parameterValues: _*
      ).largeUpdate()
    }

    def execute(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit ev: Executable,
      ev1: Preparer,
      ev2: Closable[PreparedStatement]
    ): Unit = {
      implicit val connection = this
      Update(queryText).on(
        parameterValues: _*
      ).execute()
    }
  }

  implicit def ConnectionToWrappedConnection(connection: Connection): WrappedConnection = {
    connection.underlying
  }

}
