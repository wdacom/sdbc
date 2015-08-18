package com.wda.sdbc.base.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetDateTimeFormatter {

  def offsetDateTimeFormatter: DateTimeFormatter

}
