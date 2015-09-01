package com.rocketfuel.sdbc.postgresql.jdbc

import java.time.format.{DateTimeFormatterBuilder, DateTimeFormatter}

package object implementation {
  val offsetTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
      parseCaseInsensitive().
      append(DateTimeFormatter.ISO_LOCAL_TIME).
      optionalStart().
      appendOffset("+HH:mm", "+00").
      optionalEnd().
      toFormatter
  }

  val offsetDateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
      parseCaseInsensitive().
      append(DateTimeFormatter.ISO_LOCAL_DATE).
      appendLiteral(' ').
      append(offsetTimeFormatter).
      toFormatter
  }
}
