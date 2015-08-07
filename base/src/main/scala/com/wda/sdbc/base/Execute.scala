package com.wda.sdbc.base

import com.wda.Logging

abstract class Execute[Connection] {
  self: Logging =>

  def execute()(implicit connection: Connection): Unit

}
