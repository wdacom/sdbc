package com.rocketfuel.sdbc.base.jdbc

import java.sql.PreparedStatement

import org.scalatest.FunSuite

class StringContextSpec
  extends FunSuite
  with StringContextMethods
  with DefaultSetters {

  test("empty string is identity") {
    val s = select""

    assertResult("")(s.queryText)
    assertResult("")(s.originalQueryText)
  }

  test("$i is replaced with ?") {
    val i = 3
    val s = select"$i"

    assertResult("?")(s.queryText)
    assertResult("@`0`")(s.originalQueryText)
  }

  test("${i}hi is replaced with ?hi") {
    val i = 3
    val s = select"${i}hi"

    assertResult("?hi")(s.queryText)
    assertResult("@`0`hi")(s.originalQueryText)
  }

  test("hi$i is replaced with hi?") {
    val i = 3
    val s = select"hi$i"

    assertResult("hi?")(s.queryText)
    assertResult("hi@`0`")(s.originalQueryText)
  }

  test("hi${i}hi is replaced with hi?hi") {
    val i = 3
    val s = select"hi${i}hi"

    assertResult("hi?hi")(s.queryText)
    assertResult("hi@`0`hi")(s.originalQueryText)
  }

  test("$i$i$i is replaced with ???") {
    val i = 3
    val s = select"$i$i$i"

    assertResult("???")(s.queryText)
    assertResult("@`0`@`1`@`2`")(s.originalQueryText)
  }

  test("Execute interpolation works") {
    val i = 3
    val s = execute"$i"

    assertResult(Map("0" -> Some(i)))(s.parameterValues)
  }

  test("Select interpolation works") {
    val i = 3
    val s = select"$i"

    assertResult(Map("0" -> Some(i)))(s.parameterValues)
  }

  /**
   * This method is for creating parameters out of values used in
   * a StringContext.
   * @param a
   * @return
   */
  override protected def toParameter(a: Any): Option[Any] = {
    a match {
      case null | None | Some(null) | Some(None) =>
        None
      case Some(a) =>
        Some(toParameter(a)).flatten
      case a =>
        Some(toDefaultParameter(a))
    }

  }

  implicit val ParameterGetter: Getter[ParameterValue] = new Getter[ParameterValue] {
    override def apply(v1: Row, v2: Index): Option[ParameterValue] = ???
  }

  implicit val parameterSetter: ParameterSetter = new ParameterSetter {
    def setAny(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: Any): Unit = ???
  }

}
