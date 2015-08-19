package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.rocketfuel.sdbc.base.jdbc

trait HasOffsetTimeFormatter
  extends jdbc.HasOffsetTimeFormatter {

  override val offsetTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_TIME).
    optionalStart().
    appendOffset("+HH:mm", "+00").
    optionalEnd().
    toFormatter
  }

}
