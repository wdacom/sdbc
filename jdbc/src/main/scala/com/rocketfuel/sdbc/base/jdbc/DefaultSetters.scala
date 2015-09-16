package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base.jdbc

trait DefaultSetters
  extends QBooleanImplicits
  with QByteImplicits
  with QBytesImplicits
  with QDateImplicits
  with QLocalDateImplicits
  with QBigDecimalImplicits
  with QDoubleImplicits
  with QFloatImplicits
  with QIntImplicits
  with QLongImplicits
  with QShortImplicits
  with QStringImplicits
  with QTimeImplicits
  with QLocalTimeImplicits
  with QTimestampImplicits
  with QInstantImplicits
  with QReaderImplicits
  with QInputStreamImplicits
  with QUUIDImplicits {

  val QBoolean = jdbc.QBoolean

  val QByte = jdbc.QByte

  val QBytes = jdbc.QBytes

  val QDate = jdbc.QDate

  val QLocalDate = jdbc.QLocalDate

  val QBigDecimal = jdbc.QBigDecimal

  val QDouble = jdbc.QDouble

  val QFloat = jdbc.QFloat

  val QInt = jdbc.QInt

  val QLong = jdbc.QLong

  val QShort = jdbc.QShort

  val QString = jdbc.QString

  val QTime = jdbc.QTime

  val QLocalTime = jdbc.QLocalTime

  val QTimestamp = jdbc.QTimestamp

  val QInstant = jdbc.QInstant

  val QReader = jdbc.QReader

  val QInputStream = jdbc.QInputStream

  val QUUID = jdbc.QUUID

  val toDefaultParameter: PartialFunction[Any, Any] =
    QBoolean.toParameter orElse
      QByte.toParameter orElse
      QBytes.toParameter orElse
      //Timestamp must come before Date, or else all Timestamps become Dates.
      QTimestamp.toParameter orElse
      QInstant.toParameter orElse
      //Time must come before Date, or else all Times become Dates.
      QTime.toParameter orElse
      QLocalTime.toParameter orElse
      QDate.toParameter orElse
      QLocalDate.toParameter orElse
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
