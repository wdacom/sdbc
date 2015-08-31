package com.rocketfuel.sdbc.base

case class ParameterValue(value: Any)

trait IsParameter[T, Statement, Index] {
  def set(preparedStatement: Statement, parameterIndex: Index, parameter: T): Unit
}

trait ParameterSetter[Statement, Index] {

  def setNone(
    preparedStatement: Statement,
    parameterIndex: Index
  ): Unit

  /**
   *
   * @param preparedStatement
   * @param parameterIndex
   * @param parameter The value to be set.
   * @param isParameter
   * @tparam T is a type understood by the DBMS driver.
   * @return
   */
  def setParameter[T](
    preparedStatement: Statement,
    parameterIndex: Index,
    parameter: T
  )(implicit isParameter: IsParameter[T, Statement, Index]
  ): Unit = {
    isParameter.set(preparedStatement, parameterIndex, parameter)
  }

  /**
   * Pattern match on parameters to get the IsParameter instance for
   * each value, and then call setParameter.
   *
   * This method is to be implemented on a per-DBMS basis.
   * @param preparedStatement
   * @param parameterIndex
   * @param parameter
   */
  def setAny(
    preparedStatement: Statement,
    parameterIndex: Index,
    parameter: Any
  ): Unit
}

trait ParameterValueImplicits {
  implicit def ToOptionParameterValue[T, Statement, Index](
    v: T
  )(implicit conversion: T => ParameterValue
  ): Option[ParameterValue] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T, Statement, Index](
    v: Option[T]
  )(implicit conversion: T => ParameterValue
  ): Option[ParameterValue] = {
    v.map(conversion)
  }
}
