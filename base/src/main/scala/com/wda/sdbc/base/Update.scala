package com.wda.sdbc.base

abstract class Update[UnderlyingConnection] {

  def update()(implicit updatable: Updatable[UnderlyingConnection], connection: UnderlyingConnection): Long = {
    updatable.update()
  }

}
