package com.wda.sdbc.base

import com.wda.sdbc.DBMS

import scala.collection.immutable.Seq

trait Connection {
  self: DBMS =>

  implicit class Connection(val underlying: java.sql.Connection) {

    if (DBMS.of(underlying).getClass != self.getClass) {
      throw new IllegalArgumentException("Connection is for the wrong DBMS.")
    }

    implicit val dbms: DBMS = self

    def closeQuietly() = {
      util.Try(underlying.close())
    }

    def iteratorForUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Iterator[MutableRow] = {
      SelectForUpdate(queryText).on(
        parameterValues: _*
      ).iterator()(this)
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

    def executeUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Int = {
      Update(queryText).on(
        parameterValues: _*
      ).update()(this)
    }

    def executeLargeUpdate(
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

  implicit def ConnectionToJDBCConnection(connection: Connection): java.sql.Connection = {
    connection.underlying
  }

}
