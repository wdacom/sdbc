package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.sql.PreparedStatement

import com.rocketfuel.sdbc.base.{ParameterValue, ToParameter}
import com.rocketfuel.sdbc.base.jdbc._
import org.postgresql.util.PGobject

//PostgreSQL doesn't support Byte, so we don't use the default setters.
trait Setters
  extends QPGObjectImplicits
  with QBooleanImplicits
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
  with QUUIDImplicits
  with QInstantImplicits
  with QLocalDateImplicits
  with QLocalTimeImplicits
  with QLocalDateTimeImplicits
  with PGTimeTzImplicits
  with PGTimestampTzImplicits
  with PGInetAddressImplicits
  with QXMLImplicits
  with QSQLXMLImplicits
  with QBlobImplicits
  with PGJsonImplicits {

  val toPostgresqlParameter: PartialFunction[Any, Any] =
    QBoolean.toParameter orElse
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
      QUUID.toParameter orElse
      QInstant.toParameter orElse
      QLocalDate.toParameter orElse
      QLocalTime.toParameter orElse
      QLocalDateTime.toParameter orElse
      QXML.toParameter orElse
      QSQLXML.toParameter orElse
      QBlob.toParameter orElse
      QPGObject.toParameter orElse
      QMap.toParameter

}

object QPGObject extends ToParameter {
  override val toParameter: PartialFunction[Any, Any] = {
    case i: PGobject => i
  }
}

trait QPGObjectImplicits {
  implicit val PGobjectIsParameter: IsParameter[PGobject] = new IsParameter[PGobject] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: PGobject): Unit = {
      preparedStatement.setObject(parameterIndex, parameter)
    }
  }

  implicit def PGobjectToParameterValue(value: PGobject): ParameterValue = {
    ParameterValue(value)
  }

  implicit def IsPGobjectToParameterValue[T](value: T)(implicit converter: T => PGobject): ParameterValue = {
    converter(value)
  }

}

object QMap extends ToParameter {
  override val toParameter: PartialFunction[Any, Any] = {
    case i: Map[String, String] => i
  }
}
