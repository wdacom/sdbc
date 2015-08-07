package com.wda.sdbc.base

import com.wda.sdbc.base

trait Selectable[Connection, Key, Value, S <: Select[Connection, Value]] {
  def select(key: Key): S
}

trait SelectableMethods[Connection] {

  type Select[Value] <: base.Select[Connection, Value]

  def iterator[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Connection, Key, Value, Select[Value]],
    connection: Connection
  ): Iterator[Value] = {
    selectable.select(key).iterator()
  }

  def option[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Connection, Key, Value, Select[Value]],
    connection: Connection
  ): Option[Value] = {
      iterator(key).toStream.headOption
  }

}
