package com.rocketfuel.sdbc.cassandra.datastax

import org.scalatest.FunSuite

class StringContextSpec extends FunSuite {

  test("Execute interpolation works") {
    val i = 3
    val s = execute"$i"

    assertResult(Map("0" -> Some(IntParameter(i))))(s.parameterValues)
  }

  test("Select interpolation works") {
    val i = 3
    val s = select"$i"

    assertResult(Map("0" -> Some(IntParameter(i))))(s.parameterValues)
  }

}
