package com.wda.sdbc.base

import com.wda.Logging

trait Updatable[UnderlyingConnection] {
  self: Logging =>

  def update()(implicit connection: UnderlyingConnection): Long

}
