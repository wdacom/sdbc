package com.wda.sdbc.base

trait ReadSelectUpdate[
QueryResult,
WrappedConnection <: {def close(): Unit; def prepare(query: String): PreparedStatement},
PreparedStatement <: {def close(): Unit; def execute(): Unit; def setNull(parameterIndex: Int): Unit; def executeQuery(): QueryResult},
WrappedRow
] {
  self: Row[WrappedRow, PreparedStatement] with Update[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with Select[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] =>

  trait Resources extends com.wda.Resources {

    def readSelect[T](name: String, hasParameters: Boolean = true)(implicit getter: Row => T): Select[T] = {
      val queryText = readResource("queries", name + ".sql")
      Select[T](queryText, hasParameters)
    }

    def readUpdate(name: String, hasParameters: Boolean = true): Update = {
      val queryText = readResource("queries", name + ".sql")
      Update(queryText, hasParameters)
    }

  }

}
