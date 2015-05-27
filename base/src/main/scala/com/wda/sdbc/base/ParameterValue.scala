package com.wda.sdbc.base

abstract class ParameterValue[+T, UnderlyingQuery, Index] {

  val value: T

  def set(
    query: UnderlyingQuery,
    parameterIndex: Index
  ): Unit

}

trait ParameterValueImplicits {
  implicit def ToOptionParameterValue[T, UnderlyingQuery, Index](v: T)(implicit conversion: T => ParameterValue[_, UnderlyingQuery, Index]): Option[ParameterValue[_, UnderlyingQuery, Index]] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T, UnderlyingQuery, Index](v: Option[T])(implicit conversion: T => ParameterValue[_, UnderlyingQuery, Index]): Option[ParameterValue[_, UnderlyingQuery, Index]] = {
    v.map(conversion)
  }
}
