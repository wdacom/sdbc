package com.rocketfuel.sdbc.sqlserver.jdbc

import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}

package object implementation {

  private[implementation] val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
      append(ISODateTimeFormat.date()).
      appendLiteral(' ').
      append(ISODateTimeFormat.hourMinuteSecondFraction()).
      appendLiteral(' ').
      appendTimeZoneOffset("+00:00", "+00:00", true, 2, 2).
      toFormatter
  }
}
