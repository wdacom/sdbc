package com.wda.sdbc.jdbc

trait ReadSelect {
  self: com.wda.Resources =>

  def readSelect[T](name: String, hasParameters: Boolean = true)(implicit getter: Row => T): Select[T] = {
    val queryText = readResource("queries", name + ".sql")
    Select[T](queryText, hasParameters)
  }
}

trait ReadBatch {
  self: com.wda.Resources =>

  def readUpdate(name: String, hasParameters: Boolean = true): Batch = {
    val queryText = readResource("queries", name + ".sql")
    Batch(queryText, hasParameters)
  }
}

trait ReadUpdate {
  self: com.wda.Resources =>

  def readUpdate(name: String, hasParameters: Boolean = true): Update = {
    val queryText = readResource("queries", name + ".sql")
    Update(queryText, hasParameters)
  }
}

trait ReadSelectForUpdate {
  self: com.wda.Resources =>

  def readSelectForUpdate(name: String, hasParameters: Boolean = true): SelectForUpdate = {
    val queryText = readResource("queries", name + ".sql")
    SelectForUpdate(queryText, hasParameters)
  }
}
