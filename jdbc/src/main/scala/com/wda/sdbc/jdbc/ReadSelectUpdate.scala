package com.wda.sdbc.jdbc

trait ReadSelect {
  self: com.wda.Resources =>

  def readSelect[T](name: String, hasParameters: Boolean = true)(implicit getter: Row => T): Selectable[T] = {
    val queryText = readResource("queries", name + ".sql")
    Selectable[T](queryText, hasParameters)
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
