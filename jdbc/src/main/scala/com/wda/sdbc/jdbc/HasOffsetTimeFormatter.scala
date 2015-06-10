package com.wda.sdbc.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetTimeFormatter {

  def offsetTimeFormatter: DateTimeFormatter

}
