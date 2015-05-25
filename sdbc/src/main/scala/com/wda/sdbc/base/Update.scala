package com.wda.sdbc.base

import com.wda.Logging

trait Update[UnderlyingConnection] {
  self: Logging =>

  def update()(implicit connection: UnderlyingConnection): Long

}
