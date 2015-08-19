package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base.ParameterValueImplicits
import org.scalatest._

class DefaultSettersSpec
  extends FunSuite
  with DefaultSetters
  with ParameterValueImplicits
  with StringGetter {

  test("implicit Int conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = 3")
  }

  test("implicit Option[String] conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Some(\"hello\")")
  }

  test("implicit scala.BigDecimal conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = BigDecimal(1)")
  }

  test("Row#get works") {
    assertCompiles("val row: Row = ???; val _ = row[String](???)")
  }

}
