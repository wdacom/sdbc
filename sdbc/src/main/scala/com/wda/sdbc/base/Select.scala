package com.wda.sdbc.base

import com.wda.Logging

trait Select[T, UnderlyingConnection, UnderlyingRow] {
  self: Logging =>

  def converter(row: Row[UnderlyingRow]): T

  def iterator()(implicit connection: UnderlyingConnection): Iterator[T]

}
