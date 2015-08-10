package com.wda.sdbc.base

import com.wda.sdbc.base

trait SelectableMethods[Connection, Select[Value] <: base.Select[Connection, Value]] {

  trait Selectable[Key, Value] {
    def select(key: Key): Select[Value]
  }

  def iterator[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Key, Value],
    connection: Connection
  ): Iterator[Value] = {
    selectable.select(key).iterator()
  }

  def option[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Key, Value],
    connection: Connection
  ): Option[Value] = {
      iterator(key).toStream.headOption
  }

}
