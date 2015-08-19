package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.rocketfuel.sdbc.base.jdbc

trait HasOffsetDateTimeFormatter
  extends jdbc.HasOffsetDateTimeFormatter {
  self: HasOffsetTimeFormatter =>

  override val offsetDateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_DATE).
    appendLiteral(' ').
    append(offsetTimeFormatter).
    toFormatter
  }

}
