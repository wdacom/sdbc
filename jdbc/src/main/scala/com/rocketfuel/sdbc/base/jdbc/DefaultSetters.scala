package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base.jdbc

trait DefaultSetters
  extends QBooleanImplicits
  with QByteImplicits
  with QBytesImplicits
  with QDateImplicits
  with QBigDecimalImplicits
  with QDoubleImplicits
  with QFloatImplicits
  with QIntImplicits
  with QLongImplicits
  with QShortImplicits
  with QStringImplicits
  with QTimeImplicits
  with QTimestampImplicits
  with QReaderImplicits
  with QInputStreamImplicits
  with QUUIDImplicits {

  type QBoolean = jdbc.QBoolean
  val QBoolean = jdbc.QBoolean

  type QByte = jdbc.QByte
  val QByte = jdbc.QByte

  type QBytes = jdbc.QBytes
  val QBytes = jdbc.QBytes

  type QDate = jdbc.QDate
  val QDate = jdbc.QDate

  type QBigDecimal = jdbc.QBigDecimal
  val QBigDecimal = jdbc.QBigDecimal

  type QDouble = jdbc.QDouble
  val QDouble = jdbc.QDouble

  type QFloat = jdbc.QFloat
  val QFloat = jdbc.QFloat

  type QInt = jdbc.QInt
  val QInt = jdbc.QInt

  type QLong = jdbc.QLong
  val QLong = jdbc.QLong

  type QShort = jdbc.QShort
  val QShort = jdbc.QShort

  type QString = jdbc.QString
  val QString = jdbc.QString

  type QTime = jdbc.QTime
  val QTime = jdbc.QTime

  type QTimestamp = jdbc.QTimestamp
  val QTimestamp = jdbc.QTimestamp

  type QReader = jdbc.QReader
  val QReader = jdbc.QReader

  type QInputStream = jdbc.QInputStream
  val QInputStream = jdbc.QInputStream

  type QUUID = jdbc.QUUID
  val QUUID = jdbc.QUUID

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
  with QInstantImplicits
  with QLocalDateImplicits
  with QLocalTimeImplicits
  with QLocalDateTimeImplicits
  with QOffsetDateTimeImplicits
  with QOffsetTimeImplicits {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  val toJava8DefaultParameter =
    toDefaultParameter orElse
      QInstant.toParameter orElse
      QLocalDate.toParameter orElse
      QLocalTime.toParameter orElse
      QLocalDateTime.toParameter orElse
      QOffsetDateTime.toParameter orElse
      QOffsetTime.toParameter

}
