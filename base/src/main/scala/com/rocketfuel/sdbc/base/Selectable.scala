package com.rocketfuel.sdbc.base

import com.rocketfuel.sdbc.base

trait Selectable[Key, Value, Connection, Select <: base.Select[Connection, Value]] {

  def select(key: Key): Select

}

trait SelectableMethods[Connection, Select[T] <: base.Select[Connection, T]] {

  def iterator[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Key, Value, Connection, Select[Value]],
    connection: Connection
  ): Iterator[Value] = {
    selectable.select(key).iterator()
  }

  def option[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Key, Value, Connection, Select[Value]],
    connection: Connection
  ): Option[Value] = {
      iterator(key).toStream.headOption
  }

}
