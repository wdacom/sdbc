package com.rocketfuel.sdbc.sqlserver.jdbc

import org.joda.time.{Duration, DateTimeZone, DateTime}

import org.scalatest.FunSuite

class SqlServerSpec extends FunSuite {

  test("Can parse UTC timestamps from ") {
    val asString = "2014-10-21 19:55:15.9600000 +00:00"
    val manual = new DateTime(2014, 10, 21, 19, 55, 15, 960, DateTimeZone.UTC)
    val parsed = DateTime.parse(asString, dateTimeFormatter)
    assert(new Duration(manual, parsed).getMillis == 0)
  }

  test("Can parse EST timestamps from ") {
    val asString = "2014-10-21 15:59:08.2405747 -04:00"
    val manual = new DateTime(2014, 10, 21, 15, 59, 8, 240, DateTimeZone.forOffsetHours(-4))
    val parsed = DateTime.parse(asString, dateTimeFormatter)
    assert(new Duration(manual, parsed).getMillis == 0)
  }

  test("Can parse timestamp with minute offsets from ") {
    val asString = "2014-10-21 15:59:08.2405747 -04:30"
    val manual = new DateTime(2014, 10, 21, 15, 59, 8, 240, DateTimeZone.forOffsetHoursMinutes(-4, -30))
    val parsed = DateTime.parse(asString, dateTimeFormatter)
    assert(new Duration(manual, parsed).getMillis == 0)
  }

}
