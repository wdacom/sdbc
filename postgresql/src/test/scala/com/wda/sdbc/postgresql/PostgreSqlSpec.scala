package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._
import org.joda.time.{Duration, DateTimeZone, DateTime}
import org.scalatest.FunSuite

class PostgreSqlSpec extends FunSuite {

  test("Can parse UTC timestamps from PostgreSql.") {
    val asString = "2014-10-21 20:40:56.586045+00"
    val manual = new DateTime(2014, 10, 21, 20, 40, 56, 586, DateTimeZone.UTC)
    val parsed = DateTime.parse(asString, dateTimeFormatter)
    assert(new Duration(manual, parsed).getMillis == 0)
  }

  test("Can parse EST timestamps from PostgreSql.") {
    val asString = "2014-10-21 16:39:38.549548-04"
    val manual = new DateTime(2014, 10, 21, 16, 39, 38, 549, DateTimeZone.forOffsetHours(-4))
    val parsed = DateTime.parse(asString, dateTimeFormatter)
    assert(new Duration(manual, parsed).getMillis == 0)
  }

  test("Can parse timestamp with minute offsets from ") {
    val asString = "2014-10-22 02:06:25.003825+05:30"
    val manual = new DateTime(2014, 10, 22, 2, 6, 25, 3, DateTimeZone.forOffsetHoursMinutes(5, 30))
    val parsed = DateTime.parse(asString, dateTimeFormatter)
    assert(new Duration(manual, parsed).getMillis == 0)
  }

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
