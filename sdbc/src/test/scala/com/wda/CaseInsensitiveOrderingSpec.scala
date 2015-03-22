package com.wda

import org.scalacheck.Gen
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class CaseInsensitiveOrderingSpec
  extends FunSuite
  with GeneratorDrivenPropertyChecks {

  def sameSign(x: Int, y: Int): Boolean = {
    Integer.signum(x) == Integer.signum(y)
  }

  test("Case insensitive comparison works") {
    forAll (Gen.alphaStr) { str1 =>
      forAll (Gen.alphaStr) { str2 =>
        val lower1 = str1.toLowerCase
        val lower2 = str2.toLowerCase
        assert(
          sameSign(
            lower1.compareTo(lower2),
            CaseInsensitiveOrdering.compare(str1, str2)
          )
        )
      }
    }
  }

}
