package com.rocketfuel.sdbc.sqlserver.jdbc

import java.time.{OffsetDateTime, ZoneOffset}

import org.scalatest.FunSuite

class SqlServerSpec extends FunSuite {

  test("Can parse UTC timestamps from ") {
    val asString = "2014-10-21 19:55:15.9600000 +00:00"
    val manual = OffsetDateTime.of(2014, 10, 21, 19, 55, 15, 960000000, ZoneOffset.UTC)
    val parsed = OffsetDateTime.from(offsetDateTimeFormatter.formatter.parse(asString))
    assert(manual == parsed)
  }

  test("Can parse EST timestamps from ") {
    val asString = "2014-10-21 15:59:08.2405747 -04:00"
    val manual = OffsetDateTime.of(2014, 10, 21, 15, 59, 8, 240574700, ZoneOffset.ofHours(-4))
    val parsed = OffsetDateTime.from(offsetDateTimeFormatter.formatter.parse(asString))
    assert(manual == parsed)
  }

  test("Can parse timestamp with minute offsets from ") {
    val asString = "2014-10-21 15:59:08.2405747 -04:30"
    val manual = OffsetDateTime.of(2014, 10, 21, 15, 59, 8, 240574700, ZoneOffset.ofHoursMinutes(-4, -30))
    val parsed = OffsetDateTime.from(offsetDateTimeFormatter.formatter.parse(asString))
    assert(manual == parsed)
  }

}
