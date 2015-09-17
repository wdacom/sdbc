package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.format._

package object implementation {

  private[implementation] val dateFormatter = {
    new DateTimeFormatterBuilder().
      appendYear(4, 4).
      appendLiteral('-').
      appendMonthOfYear(2).
      appendLiteral('-').
      appendDayOfMonth(2).
      toFormatter
  }

  private[implementation] val datePrinter = dateFormatter.getPrinter

  private[implementation] val dateParser = dateFormatter.getParser

  private[implementation] val timeParser = {
    new DateTimeFormatterBuilder().
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
      toParser
  }

  val timeFormatter = {
    new DateTimeFormatterBuilder().
      appendHourOfDay(2).
      appendLiteral(':').
      appendMinuteOfHour(2).
      appendLiteral(':').
      appendSecondOfMinute(2).
      appendLiteral('.').
      appendFractionOfSecond(1, 6).
      toFormatter
  }

  val timePrinter = timeFormatter.getPrinter

  private[jdbc] val timetzFormatter = {
    new DateTimeFormatterBuilder().
      append(timePrinter, timeParser).
      appendTimeZoneOffset("+00", "+00", true, 1, 2).
      toFormatter
  }

  private[jdbc] val timetzPrinter = {
    timetzFormatter.getPrinter
  }

  private[jdbc] val timetzParser = {
    timetzFormatter.getParser
  }

  private[implementation] val timestamptzFormatter = {
    new DateTimeFormatterBuilder().
      append(datePrinter, dateParser).
      appendLiteral(' ').
      append(timetzPrinter, timetzParser).
      toFormatter
  }

  private[implementation] val datetimetzFormatter = {
    new DateTimeFormatterBuilder().
    append(datePrinter, dateParser).
    appendLiteral(' ').
    append(timetzPrinter, timetzParser).
    toFormatter
  }

  private[implementation] val datetimetzPrinter = {
    datetimetzFormatter.getPrinter
  }

  private[implementation] val datetimetzParser = {
    datetimetzFormatter.getParser
  }

}
