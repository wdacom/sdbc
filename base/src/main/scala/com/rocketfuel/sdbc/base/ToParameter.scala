package com.rocketfuel.sdbc.base

trait ToParameter {

  val toParameter: PartialFunction[Any, Any]
  
}
