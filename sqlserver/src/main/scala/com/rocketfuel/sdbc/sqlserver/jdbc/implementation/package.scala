package com.rocketfuel.sdbc.sqlserver.jdbc

import org.joda.time.format._

package object implementation {

  private[implementation] val dateTimeParser: DateTimeParser = {
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
      toParser
  }

  private[implementation] val dateTimePrinter: DateTimePrinter = {
    new DateTimeFormatterBuilder().
    append(ISODateTimeFormat.date()).
    appendLiteral(' ').
    appendHourOfDay(2).
    appendLiteral(":").
    appendMinuteOfHour(2).
    appendLiteral(":").
    appendSecondOfMinute(2).
    appendLiteral('.').
    appendFractionOfSecond(1, 7).
    appendLiteral(' ').
    appendTimeZoneOffset("+00:00", "+00:00", true, 2, 2).
    toPrinter
  }

  private[implementation] val dateTimeFormatter = {
    new DateTimeFormatter(dateTimePrinter, dateTimeParser)
  }

}
