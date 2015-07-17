package com.wda.sdbc.base

trait Executable[Connection, Key] {
  def execute(key: Key)(implicit connection: Connection): Unit
}

trait ExecutableMethods[Connection] {
  def execute[Key](key: Key)(implicit ev: Executable[Connection, Key], connection: Connection): Unit = {
    ev.execute(key)
  }
}
