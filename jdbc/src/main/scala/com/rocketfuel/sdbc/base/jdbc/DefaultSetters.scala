package com.rocketfuel.sdbc.base.jdbc

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

}

trait Java8DefaultSetters
  extends DefaultSetters
  with InstantParameter
  with LocalDateParameter
  with LocalTimeParameter
  with LocalDateTimeParameter
  with OffsetDateTimeParameter
  with OffsetTimeParameter {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

}
