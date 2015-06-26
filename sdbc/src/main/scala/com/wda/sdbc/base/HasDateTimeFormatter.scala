package com.wda.sdbc.base

import org.joda.time.format.DateTimeFormatter

trait HasDateTimeFormatter {
  def dateTimeFormatter: DateTimeFormatter
}
