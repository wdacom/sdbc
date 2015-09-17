package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}

package object implementation {
  private[implementation] val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
      append(ISODateTimeFormat.date()).
      appendLiteral(' ').
      appendHourOfDay(2).
      appendLiteral(':').
      appendMinuteOfHour(2).
      appendLiteral(':').
      appendSecondOfMinute(2).
      appendOptional(
        new DateTimeFormatterBuilder().
        appendLiteral('.').
        appendFractionOfSecond(1, 6).
        toParser
      ).
      appendTimeZoneOffset("+00", "+00", true, 1, 2).
      toFormatter
  }
}
