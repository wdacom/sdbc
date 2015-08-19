package com.rocketfuel.sdbc.base

import com.rocketfuel.Logging

abstract class Batch[UnderlyingConnection] extends Select[UnderlyingConnection, Long] {
  self: Logging =>

  override def iterator()(implicit connection: UnderlyingConnection): Iterator[Long]

}
