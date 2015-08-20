package com.rocketfuel.sdbc.base

import com.rocketfuel.Logging

abstract class Execute[Connection] {
  self: Logging =>

  def execute()(implicit connection: Connection): Unit

}
