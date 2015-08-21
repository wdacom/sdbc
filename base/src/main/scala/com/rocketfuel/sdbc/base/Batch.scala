package com.rocketfuel.sdbc.base

import com.rocketfuel.Logging

abstract class Batch[Connection] extends Select[Connection, Long] {
  self: Logging =>

  override def iterator()(implicit connection: Connection): Iterator[Long]

}
