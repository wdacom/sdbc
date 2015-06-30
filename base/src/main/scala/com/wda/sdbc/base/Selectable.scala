package com.wda.sdbc.base

trait Selectable[Connection, Key, Value] {
  def select(key: Key)(implicit connection: Connection): Iterator[Value]
}

trait SelectableMethods[Connection, Value] {

  def select[Key](key: Key)(implicit ev: Selectable[Connection, Key, Value], connection: Connection): Iterator[Value] = {
    ev.select(key)
  }

  def option[Key](key: Key)(implicit ev: Selectable[Connection, Key, Value], connection: Connection): Option[Value] = {
      select(key).toStream.headOption
  }

}
