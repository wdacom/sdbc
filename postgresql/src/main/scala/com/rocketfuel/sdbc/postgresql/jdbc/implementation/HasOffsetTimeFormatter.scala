package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.base.jdbc.SdbcOffsetTimeFormatter

trait HasOffsetTimeFormatter
  extends jdbc.HasOffsetTimeFormatter {

  override implicit val offsetTimeFormatter: SdbcOffsetTimeFormatter = {
    val formatter = new DateTimeFormatterBuilder().
      parseCaseInsensitive().
      append(DateTimeFormatter.ISO_LOCAL_TIME).
      optionalStart().
      appendOffset("+HH:mm", "+00").
      optionalEnd().
      toFormatter

    SdbcOffsetTimeFormatter(formatter)
  }

}
