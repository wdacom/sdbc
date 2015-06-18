package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._
import org.scalatest.FunSuite

class PostgreSqlSpec extends FunSuite {

  test("Can quote a single identifier.") {
    val manual = "identifier"
    val quoted = Identifier.quote("identifier")
    assert(manual == quoted)
  }

  test("Can quote a single complex identifier.") {
    val manual = "\"complex identifier\""
    val quoted = Identifier.quote("complex identifier")
    assert(manual == quoted)
  }

  test("Can quote a two part identifier.") {
    val manual = """"first part"."second part""""
    val quoted = Identifier.quote("first part", "second part")
    assert(manual == quoted)
  }

  test("Can quote a three part identifier.") {
    val manual = """"first part"."second part"."third part""""
    val quoted = Identifier.quote("first part", "second part", "third part")
    assert(manual == quoted)
  }

  test("Escapes double quotes.") {
    val tests =
      Seq(
        (Seq("""has"quote"""), """"has""quote""""),
        (Seq("""has"quote""", "noHasQuote"), """"has""quote".noHasQuote"""),
        (Seq("""has"double""quotes""", "noHasQuote"), "\"has\"\"double\"\"\"\"quotes\".noHasQuote")
      )
    for ((identifier, expectedResult) <- tests) {
      val quoted = Identifier.quote(identifier: _*)
      assert(quoted == expectedResult)
    }
  }

  test("Empty identifiers are not allowed.") {
    intercept[IllegalArgumentException] {
      Identifier.quote("")
    }
  }

}
