package com.wda.sdbc
package postgresql

import org.postgresql.util.PGInterval
import org.scalatest.FunSuite

class PGIntervalSpec extends FunSuite {

  val str = "9 years 11 mons 29 days 06:41:38.636266"

  val asPg = new PGInterval(str)

  val asPgParts = new PGInterval(9, 11, 29, 6, 41, 38.636266)

  test("PGInterval created from string equals PGInterval created from parts") {
    assert(asPg.getValue == asPgParts.getValue)
  }

}
