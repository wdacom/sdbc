package com.rocketfuel.sdbc.base.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetDateTimeFormatter {

  implicit def SdbcOffsetDateTimeParameterToDateTimeFormatter(formatter: SdbcOffsetDateTimeFormatter): DateTimeFormatter = {
    formatter.formatter
  }

  implicit def offsetDateTimeFormatter: SdbcOffsetDateTimeFormatter

}
