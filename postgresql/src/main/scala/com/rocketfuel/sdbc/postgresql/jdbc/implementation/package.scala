package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}

package object implementation {
  private[implementation] val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
      append(ISODateTimeFormat.date()).
      appendLiteral(' ').
      append(ISODateTimeFormat.hourMinuteSecondFraction()).
      appendTimeZoneOffset("+00", "+00", true, 1, 2).
      toFormatter
  }
}
