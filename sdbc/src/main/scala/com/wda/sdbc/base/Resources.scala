package com.wda.sdbc.base

trait Resources {
  self: Row with Update with Select with SelectForUpdate with Batch =>

  trait Resources extends com.wda.Resources {

    def readSelect[T](name: String, hasParameters: Boolean = true)(implicit getter: Row => T): Select[T] = {
      val queryText = readResource("queries", name + ".sql")
      Select[T](queryText, hasParameters)
    }

    def readSelectForUpdate(name: String, hasParameters: Boolean = true): SelectForUpdate = {
      val queryText = readResource("queries", name + ".sql")
      SelectForUpdate(queryText, hasParameters)
    }

    def readUpdate(name: String, hasParameters: Boolean = true): Update = {
      val queryText = readResource("queries", name + ".sql")
      Update(queryText, hasParameters)
    }

    def readBatch(name: String, hasParameters: Boolean = true): Batch = {
      val queryText = readResource("queries", name + ".sql")
      Batch(queryText, hasParameters)
    }

  }

}
