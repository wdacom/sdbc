package com.wda.sdbc.base

abstract class ParameterValue[+T, UnderlyingQuery] {

  val value: T

  def set(
    query: UnderlyingQuery,
    parameterIndex: Int
  ): Unit
}

trait ParameterValueImplicits {
  implicit def ToOptionParameterValue[T, UnderlyingQuery](v: T)(implicit conversion: T => ParameterValue[_, UnderlyingQuery]): Option[ParameterValue[_, UnderlyingQuery]] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T, UnderlyingQuery](v: Option[T])(implicit conversion: T => ParameterValue[_, UnderlyingQuery]): Option[ParameterValue[_, UnderlyingQuery]] = {
    v.map(conversion)
  }
}
