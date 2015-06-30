package com.wda.sdbc.base

trait Updatable[Connection, Key] {
  def update(key: Key)(implicit connection: Connection): Long
}

trait UpdatableMethods[Connection] {
  def update[Key](key: Key)(implicit ev: Updatable[Connection, Key], connection: Connection): Long = {
    ev.update(key)
  }
}
