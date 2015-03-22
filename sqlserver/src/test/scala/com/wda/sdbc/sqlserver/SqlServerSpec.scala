package com.wda.sdbc.sqlserver

import java.time.{OffsetDateTime, ZoneOffset}

import com.wda.sdbc.SqlServer._
import org.scalatest.FunSuite

class SqlServerSpec extends FunSuite {
  test("Can parse UTC timestamps from ") {
    val asString = "2014-10-21 19:55:15.9600000 +00:00"
    val manual = OffsetDateTime.of(2014, 10, 21, 19, 55, 15, 960000000, ZoneOffset.UTC)
    val parsed = OffsetDateTime.from(dateTimeFormatter.parse(asString))
    assert(manual == parsed)
  }

  test("Can parse EST timestamps from ") {
    val asString = "2014-10-21 15:59:08.2405747 -04:00"
    val manual = OffsetDateTime.of(2014, 10, 21, 15, 59, 8, 240574700, ZoneOffset.ofHours(-4))
    val parsed = OffsetDateTime.from(dateTimeFormatter.parse(asString))
    assert(manual == parsed)
  }

  test("Can parse timestamp with minute offsets from ") {
    val asString = "2014-10-21 15:59:08.2405747 -04:30"
    val manual = OffsetDateTime.of(2014, 10, 21, 15, 59, 8, 240574700, ZoneOffset.ofHoursMinutes(-4, -30))
    val parsed = OffsetDateTime.from(dateTimeFormatter.parse(asString))
    assert(manual == parsed)
  }

  test("Can quote a single identifier.") {
    val manual = "identifier"
    val quoted = Identifier.quote("identifier")
    assert(manual == quoted)
  }

  test("Can quote a single complex identifier.") {
    val manual = "[complex identifier]"
    val quoted = Identifier.quote("complex identifier")
    assert(manual == quoted)
  }

  test("Can quote a two part identifier.") {
    val manual = "[first part].[second part]"
    val quoted = Identifier.quote("first part", "second part")
    assert(manual == quoted)
  }

  test("Can quote a three part identifier.") {
    val manual = "[first part].[second part].[third part]"
    val quoted = Identifier.quote("first part", "second part", "third part")
    assert(manual == quoted)
  }

  test("Escapes double quotes.") {
    val tests =
      Seq(
        (Seq("has]quote"), "[has]]quote]"),
        (Seq("has]quote", "noHasQuote"), "[has]]quote].noHasQuote"),
        (Seq("has]double]]quotes", "noHasQuote"), "[has]]double]]]]quotes].noHasQuote")
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
