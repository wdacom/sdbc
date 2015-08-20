package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.{DateTime, DateTimeZone}

import org.scalatest.FunSuite

class PostgreSqlSpec extends FunSuite {

  test("Can parse UTC timestamps from PostgreSql.") {
    val asString = "2014-10-21 20:40:56.586045+00"
    val manual = new DateTime(2014, 10, 21, 20, 40, 56, 586045000, DateTimeZone.UTC)
    val parsed = dateTimeFormatter.parseDateTime(asString)
    assert(manual == parsed)
  }

  test("Can parse EST timestamps from PostgreSql.") {
    val asString = "2014-10-21 16:39:38.549548-04"
    val manual = new DateTime(2014, 10, 21, 16, 39, 38, 549548000, DateTimeZone.forOffsetHours(-4))
    val parsed = dateTimeFormatter.parseDateTime(asString)
    assert(manual == parsed)
  }

  test("Can parse timestamp with minute offsets from ") {
    val asString = "2014-10-22 02:06:25.003825+05:30"
    val manual = new DateTime(2014, 10, 22, 2, 6, 25, 3825000, DateTimeZone.forOffsetHoursMinutes(5, 30))
    val parsed = dateTimeFormatter.parseDateTime(asString)
    assert(manual == parsed)
  }

}
