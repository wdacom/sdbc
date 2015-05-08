package com.wda.sdbc.jdbc

import com.wda.sdbc.base.Row

trait DefaultSetters
  extends BooleanParameter
  with ByteParameter
  with BytesParameter
  with DateParameter
  with DecimalParameter
  with DoubleParameter
  with FloatParameter
  with IntParameter
  with LongParameter
  with ShortParameter
  with StringParameter
  with TimeParameter
  with TimestampParameter
  with ReaderParameter
  with InputStreamParameter
  with UUIDParameter {
  self: JdbcParameterValue with Row =>

}

trait Java8DefaultSetters
  extends DefaultSetters
  with InstantParameter
  with LocalDateParameter
  with LocalTimeParameter
  with LocalDateTimeParameter {
  self: JdbcParameterValue with Row =>

}
