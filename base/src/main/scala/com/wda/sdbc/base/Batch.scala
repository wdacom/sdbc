package com.wda.sdbc.base

import com.wda.Logging

abstract class Batch[UnderlyingConnection] extends Select[Long, UnderlyingConnection] {
  self: Logging =>

  override def iterator()(implicit connection: UnderlyingConnection): Iterator[Long]

}
