package com.rocketfuel.sdbc.sqlserver.jdbc

import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}

package object implementation {

  private[implementation] val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
      append(ISODateTimeFormat.date()).
      appendLiteral(' ').
      appendHourOfDay(2).
      appendLiteral(":").
      appendMinuteOfHour(2).
      appendLiteral(":").
      appendSecondOfMinute(2).
      appendOptional(
        new DateTimeFormatterBuilder().
          appendLiteral('.').
          appendFractionOfSecond(1, 7).
          toParser
      ).
      appendLiteral(' ').
      appendTimeZoneOffset("+00:00", "+00:00", true, 2, 2).
      toFormatter
  }
}
