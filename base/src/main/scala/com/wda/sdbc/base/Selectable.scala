package com.wda.sdbc.base

trait Selectable[Connection, Key, Value] {
  def select(key: Key): Select[Connection, Value]
}

trait SelectableMethods[Connection, Value] {

  def iterator[Key](
    key: Key
  )(implicit selectable: Selectable[Connection, Key, Value],
    connection: Connection
  ): Iterator[Value] = {
    selectable.select(key).iterator()
  }

  def option[Key](
    key: Key
  )(implicit selectable: Selectable[Connection, Key, Value],
    connection: Connection
  ): Option[Value] = {
      iterator(key).toStream.headOption
  }

}
