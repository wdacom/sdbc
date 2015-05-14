package com.wda.sdbc.base

trait ReadSelect[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] {
  self: Select[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] with com.wda.Resources =>

  def readSelect[T](name: String, hasParameters: Boolean = true)(implicit getter: Row[UnderlyingRow] => T): Select[T] = {
    val queryText = readResource("queries", name + ".sql")
    Select[T](queryText, hasParameters)
  }
}

trait ReadUpdate[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] {
  self: com.wda.Resources =>

  def readUpdate(name: String, hasParameters: Boolean = true): Update[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
    val queryText = readResource("queries", name + ".sql")
    Update(queryText, hasParameters)
  }
}

trait ReadSelectForUpdate[UnderlyingConnection, PreparedStatement, MutableResultSet, UnderlyingMutableRow] {
  self: com.wda.Resources with SelectForUpdate[UnderlyingConnection, PreparedStatement, MutableResultSet, UnderlyingMutableRow] =>

  def readSelectForUpdate(name: String, hasParameters: Boolean = true): SelectForUpdate = {
    val queryText = readResource("queries", name + ".sql")
    SelectForUpdate(queryText, hasParameters)
  }
}

trait ReadBatch {
  self: com.wda.Resources =>

  def readBatch(name: String, hasParameters: Boolean = true): Batch = {
    val queryText = readResource("queries", name + ".sql")
    Batch(queryText, hasParameters)
  }
}
