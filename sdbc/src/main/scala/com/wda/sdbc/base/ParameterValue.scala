package com.wda.sdbc.base

abstract class ParameterValue[T, PreparedStatement] {

  val value: T

  def asJDBCObject: AnyRef

  def set(
    preparedStatement: PreparedStatement,
    parameterIndex: Int
  ): Unit
}

trait ParameterValueImplicits {
  implicit def ToOptionParameterValue[T, PreparedStatement](v: T)(implicit conversion: T => ParameterValue[_, PreparedStatement]): Option[ParameterValue[_, PreparedStatement]] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T, PreparedStatement](v: Option[T])(implicit conversion: T => ParameterValue[_, PreparedStatement]): Option[ParameterValue[_, PreparedStatement]] = {
    v.map(conversion)
  }
}
