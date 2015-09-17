package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.FunSuite

class TimeTzFormatterSpec extends FunSuite {
  test("Can parse UTC timestamps from PostgreSql.") {
    val asString = "20:40:56.586045+00"
    val manual = new DateTime(1970, 1, 1, 20, 40, 56, 586, DateTimeZone.UTC).toDateTime(DateTimeZone.UTC)
    val parsed = TimeTz.formatter.parseDateTime(asString).toDateTime(DateTimeZone.UTC)
    assert(manual == parsed)
  }

  test("Can parse EST timestamps from PostgreSql.") {
    val asString = "16:39:38.549548-04"
    val manual = new DateTime(1970, 1, 1, 16, 39, 38, 549, DateTimeZone.forOffsetHours(-4)).toDateTime(DateTimeZone.UTC)
    val parsed = TimeTz.formatter.parseDateTime(asString).toDateTime(DateTimeZone.UTC)
    assert(manual == parsed)
  }

  test("Can parse timestamp with minute offsets from PostgreSql") {
    val asString = "02:06:25.003825+05:30"
    val manual = new DateTime(1970, 1, 1, 2, 6, 25, 3, DateTimeZone.forOffsetHoursMinutes(5, 30)).toDateTime(DateTimeZone.UTC)
    val parsed = TimeTz.formatter.parseDateTime(asString).toDateTime(DateTimeZone.UTC)
    assert(manual == parsed)
  }

  test("Can parse timestamp with no fractional seconds from PostgreSql") {
    val asString = "02:06:25+05:30"
    val manual = new DateTime(1970, 1, 1, 2, 6, 25, 0, DateTimeZone.forOffsetHoursMinutes(5, 30)).toDateTime(DateTimeZone.UTC)
    val parsed = TimeTz.formatter.parseDateTime(asString).toDateTime(DateTimeZone.UTC)
    assert(manual == parsed)
  }

}
