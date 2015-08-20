package com.rocketfuel.sdbc.base.jdbc

import org.joda.time.format.DateTimeFormatter

trait HasDateTimeFormatter {

  def dateTimeFormatter: DateTimeFormatter

}
