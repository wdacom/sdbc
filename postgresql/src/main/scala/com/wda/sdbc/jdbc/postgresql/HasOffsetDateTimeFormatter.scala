package com.wda.sdbc.jdbc.postgresql

import java.time.format.{DateTimeFormatterBuilder, DateTimeFormatter}

import com.wda.sdbc.base.jdbc

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
