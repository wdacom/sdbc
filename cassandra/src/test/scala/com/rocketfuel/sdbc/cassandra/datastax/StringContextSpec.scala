package com.rocketfuel.sdbc.cassandra.datastax

import org.scalatest.FunSuite

class StringContextSpec extends FunSuite {

  test("Int interpolation works") {
    val i = 3
    val j = 4
    val s = execute"$i$i$j"

    assertResult(Map("0" -> Some(IntParameter(3)), "1" -> Some(IntParameter(3)), "2" -> Some(IntParameter(4))))(s.parameterValues)
  }

  test("Set interpolation works") {
    val i = Set(3)
    val j = Set(4)
    val s = execute"$i$i$j"

    assertResult(Map("0" -> Some(SetParameter(Set(3))), "1" -> Some(SetParameter(Set(3))), "2" -> Some(SetParameter(Set(4)))))(s.parameterValues)
  }

  test("Int interpolation works with select") {
    val i = 3
    val j = 4
    val s = select"$i$i$j"

    assertResult(Map("0" -> Some(IntParameter(3)), "1" -> Some(IntParameter(3)), "2" -> Some(IntParameter(4))))(s.parameterValues)
  }

}
