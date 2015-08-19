package com.rocketfuel.sdbc.base.jdbc

import java.time.format.DateTimeFormatter

trait HasOffsetTimeFormatter {

  def offsetTimeFormatter: DateTimeFormatter

}
