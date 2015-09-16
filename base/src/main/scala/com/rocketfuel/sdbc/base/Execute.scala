package com.rocketfuel.sdbc.base

abstract class Execute[Connection] {
  self: Logging =>

  def execute()(implicit connection: Connection): Unit

}
