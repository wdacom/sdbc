package com.wda.sdbc.base

import com.wda.sdbc.jdbc.DefaultSetters
import org.scalatest._

class DefaultSettersSpec
  extends FunSuite
  with Row
  with Getter
  with JdbcParameterValue
  with DefaultSetters {

  test("implicit Int conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = 3")
  }

  test("implicit Option[String] conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Some(\"hello\")")
  }

  test("implicit scala.BigDecimal conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = BigDecimal(1)")
  }

}
