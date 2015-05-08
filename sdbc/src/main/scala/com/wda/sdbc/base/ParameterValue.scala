package com.wda.sdbc.base

import java.sql.{Array => _, _}

trait ParameterValue[WrappedRow, PreparedStatement] {
  self: Row[WrappedRow, PreparedStatement] =>

  abstract class ParameterValue[T] {

    val value: T

    def asJDBCObject: AnyRef

    def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit
  }


  implicit def ToOptionParameterValue[T](v: T)(implicit conversion: T => ParameterValue[_]): Option[ParameterValue[_]] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T](v: Option[T])(implicit conversion: T => ParameterValue[_]): Option[ParameterValue[_]] = {
    v.map(conversion)
  }

}
