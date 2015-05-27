package com.wda.sdbc.base

import com.wda.Logging

trait Selectable[T, UnderlyingConnection] {
  self: Logging =>

  def iterator()(implicit connection: UnderlyingConnection): Iterator[T]

}
