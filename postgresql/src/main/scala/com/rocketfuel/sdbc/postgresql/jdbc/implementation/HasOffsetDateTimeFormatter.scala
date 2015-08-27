package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.base.jdbc.SdbcOffsetDateTimeFormatter

trait HasOffsetDateTimeFormatter
  extends jdbc.HasOffsetDateTimeFormatter {
  self: HasOffsetTimeFormatter =>

  override implicit val offsetDateTimeFormatter: SdbcOffsetDateTimeFormatter = {
    val formatter = new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_DATE).
    appendLiteral(' ').
    append(offsetTimeFormatter.formatter).
    toFormatter

    SdbcOffsetDateTimeFormatter(formatter)
  }

}
