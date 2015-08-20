package com.rocketfuel.sdbc.base

import com.rocketfuel.Logging

abstract class Select[Connection, T] {
  self: Logging =>

  def iterator()(implicit connection: Connection): Iterator[T]

}
