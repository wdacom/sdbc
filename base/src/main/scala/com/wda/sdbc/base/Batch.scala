package com.wda.sdbc.base

import com.wda.Logging

abstract class Batch[UnderlyingConnection] extends Select[UnderlyingConnection, Long] {
  self: Logging =>

  override def iterator()(implicit connection: UnderlyingConnection): Iterator[Long]

}
