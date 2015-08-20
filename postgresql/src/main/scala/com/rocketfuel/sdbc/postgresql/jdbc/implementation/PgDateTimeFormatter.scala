package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc.HasDateTimeFormatter
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}

trait PgDateTimeFormatter
  extends HasDateTimeFormatter {

  override val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
    append(ISODateTimeFormat.date()).
    appendLiteral(' ').
    append(ISODateTimeFormat.hourMinuteSecondFraction()).
    appendTimeZoneOffset("+00", "+00", true, 1, 2).
    toFormatter
  }

}
