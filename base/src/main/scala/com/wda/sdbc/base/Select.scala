package com.wda.sdbc.base

import com.wda.Logging

abstract class Select[T, Connection] {
  self: Logging =>

  def iterator()(implicit connection: Connection): Iterator[T]

  def execute()(implicit connection: Connection): Unit

}
