package com.rocketfuel.sdbc.base.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetTimeFormatter {

  implicit def SdbcOffsetTimeParameterToDateTimeFormatter(formatter: SdbcOffsetTimeFormatter): DateTimeFormatter = {
    formatter.formatter
  }

  implicit def offsetTimeFormatter: SdbcOffsetTimeFormatter

}
