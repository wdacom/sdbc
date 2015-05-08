package com.wda.sdbc.base

trait ReadSelectUpdate {
  self: Row with Update with Select =>

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
