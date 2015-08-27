package com.rocketfuel.sdbc.base.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetDateTimeFormatter {

  implicit def offsetDateTimeFormatter: SdbcOffsetDateTimeFormatter

}
