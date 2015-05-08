package com.wda.sdbc.base

import scala.collection.immutable.Seq
import scala.language.reflectiveCalls

trait Connection[
  QueryResult,
  WrappedConnection <: {def close(): Unit},
  PreparedStatement <: {def close(): Unit; def execute(): Unit; def setNull(parameterIndex: Int): Unit; def executeUpdate(): Int; def executeLargeUpdate(): Long},
  WrappedRow
] {
  self: AbstractQuery[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with Row[WrappedRow, PreparedStatement] with ParameterValue[WrappedRow, PreparedStatement] with Select[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with Update[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] =>

  abstract class Connection(val underlying: WrappedConnection) {

    def prepareStatement(query: String): PreparedStatement

    def closeQuietly() = {
      util.Try(underlying.close())
    }

    def iterator[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit converter: Row => T
    ): Iterator[T] = {
      Select[T](queryText).on(
        parameterValues: _*
      ).iterator()(this)
    }

    def seq[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(
      implicit converter: Row => T
    ): Seq[T] = {
      Select[T](queryText).on(
        parameterValues: _*
      ).seq()(this)
    }

    def option[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(
      implicit converter: Row => T
    ): Option[T] = {
      Select[T](queryText).on(
        parameterValues: _*
      ).option()(this)
    }

    def single[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(
      implicit converter: Row => T
    ): T = {
      Select[T](queryText).on(
        parameterValues: _*
      ).single()(this)
    }

    def update(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Int = {
      Update(queryText).on(
        parameterValues: _*
      ).update()(this)
    }

    def largeUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Long = {
      Update(queryText).on(
        parameterValues: _*
      ).largeUpdate()(this)
    }

    def execute(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Unit = {
      Update(queryText).on(
        parameterValues: _*
      ).execute()(this)
    }
  }

  implicit def ConnectionToWrappedConnection(connection: Connection): WrappedConnection = {
    connection.underlying
  }

}
