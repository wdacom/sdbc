package com.rocketfuel.sdbc.base

abstract class ParameterValue[+T, Statement, Index] {

  val value: T

  def set(
    statement: Statement,
    parameterIndex: Index
  ): Unit

}

trait ParameterValueImplicits {
  implicit def ToOptionParameterValue[T, Statement, Index](v: T)(implicit conversion: T => ParameterValue[_, Statement, Index]): Option[ParameterValue[_, Statement, Index]] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T, Statement, Index](v: Option[T])(implicit conversion: T => ParameterValue[_, Statement, Index]): Option[ParameterValue[_, Statement, Index]] = {
    v.map(conversion)
  }
}
