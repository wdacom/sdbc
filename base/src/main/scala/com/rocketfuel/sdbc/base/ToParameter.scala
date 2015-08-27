package com.rocketfuel.sdbc.base

trait ToParameter[Statement, Index] {

  val toParameter: PartialFunction[Any, ParameterValue[_, Statement, Index]]
  
}
