package com.wda.sdbc.base

import com.wda.Logging

abstract class Update[UnderlyingConnection] extends Batch[UnderlyingConnection] {
  self: Logging =>

  def update()(implicit connection: UnderlyingConnection): Long

  override def iterator()(implicit connection: UnderlyingConnection): Iterator[Long] = {
    Iterator(update())
  }

}
