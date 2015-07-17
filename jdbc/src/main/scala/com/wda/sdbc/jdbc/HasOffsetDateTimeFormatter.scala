package com.wda.sdbc.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetDateTimeFormatter {

  def offsetDateTimeFormatter: DateTimeFormatter

}
