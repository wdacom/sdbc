package com.rocketfuel.sdbc.base.jdbc

trait DefaultSetters
  extends BooleanParameter
  with ByteParameter
  with BytesParameter
  with DateParameter
  with BigDecimalParameter
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

  val toDefaultParameter: PartialFunction[Any, ParameterValue[_]] =
    QBoolean.toParameter orElse
      QByte.toParameter orElse
      QBytes.toParameter orElse
      //Timestamp must come before Date, or else all Timestamps become Dates.
      QTimestamp.toParameter orElse
      //Time must come before Date, or else all Times become Dates.
      QTime.toParameter orElse
      QDate.toParameter orElse
      QBigDecimal.toParameter orElse
      QDouble.toParameter orElse
      QFloat.toParameter orElse
      QInt.toParameter orElse
      QLong.toParameter orElse
      QShort.toParameter orElse
      QString.toParameter orElse
      QReader.toParameter orElse
      QInputStream.toParameter orElse
      QUUID.toParameter

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

  val toJava8DefaultParameter =
    toDefaultParameter orElse
      toInstantParameter orElse
      toLocalDateParameter orElse
      toLocalTimeParameter orElse
      toLocalDateTimeParameter orElse
      toOffsetDateTimeParameter orElse
      toOffsetTimeParameter

}
