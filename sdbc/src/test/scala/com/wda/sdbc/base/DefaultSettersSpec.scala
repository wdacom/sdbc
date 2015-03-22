package com.wda.sdbc.base

import org.scalatest._

class DefaultSettersSpec
  extends FunSuite
  with Row
  with Getter
  with ParameterValue
  with DefaultSetters {

  test("implicit Int conversion works") {
    assertCompiles("val _: Option[ParameterValue[Int]] = 3")
  }

  test("implicit Option[String] conversion works") {
    assertCompiles("val _: Option[ParameterValue[String]] = Some(\"hello\")")
  }

  test("implicit scala.BigDecimal conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = BigDecimal(1)")
  }

}
