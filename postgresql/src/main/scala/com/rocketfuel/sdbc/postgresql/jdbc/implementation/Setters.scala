package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.PreparedStatement

import com.rocketfuel.sdbc.postgresql.jdbc.implementation
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.postgresql.jdbc.LTree
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.Node

//PostgreSQL doesn't support Byte, so we don't use the default setters.
trait Setters
  extends QBooleanImplicits
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
  with QOffsetDateTimeImplicits
  with QOffsetTimeImplicits
  with QInetAddressImplicits
  with QXMLImplicits {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  type QBoolean = jdbc.QBoolean
  val QBoolean = jdbc.QBoolean

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

  type QOffsetTime = jdbc.QOffsetTime
  val QOffsetTime = jdbc.QOffsetTime

  type QOffsetDateTime = jdbc.QOffsetDateTime
  val QOffsetDateTime = jdbc.QOffsetDateTime

  type QInetAddress = implementation.QInetAddress
  val QInetAddress = implementation.QInetAddress

  type QPGInterval = implementation.QPGInterval
  val QPGInterval = implementation.QPGInterval

  type QJSON = implementation.QJSON
  val QJSON = implementation.QJSON

  type QLTree = implementation.QLTree
  val QLTree = implementation.QLTree

  type QXML = implementation.QXML
  val QXML = implementation.QXML

  val toPostgresqlParameter: PartialFunction[Any, ParameterValue[_]] =
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
      QOffsetDateTime.toParameter orElse
      QOffsetTime.toParameter orElse
      QInetAddress.toParameter orElse
      QPGInterval.toParameter orElse
      QJSON.toParameter orElse
      QLTree.toParameter orElse
      QXML.toParameter

}

case class QInetAddress(value: InetAddress) extends ParameterValue[InetAddress] {

  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setString(parameterIndex, value.getHostAddress)
  }

}

object QInetAddress extends ToParameter with QInetAddressImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case i: InetAddress => i
  }
}

trait QInetAddressImplicits {
  implicit def InetAddressToParameterValue(x: InetAddress): ParameterValue[InetAddress] = {
    QInetAddress(x)
  }
}


case class QPGInterval(value: PGInterval) extends ParameterValue[PGInterval] {
  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setObject(parameterIndex, value)
  }
}

object QPGInterval extends ToParameter with QPGIntervalImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case p: PGInterval => p
  }
}

trait QPGIntervalImplicits {
  implicit def PGIntervalToParameterValue(x: PGInterval): ParameterValue[PGInterval] = {
    QPGInterval(x)
  }
}

case class QJSON(value: JValue)(implicit formats: Formats) extends ParameterValue[JValue] {

  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setString(parameterIndex, JsonMethods.compact(JsonMethods.render(value)))
  }

  def withFormats(implicit newFormats: Formats): QJSON = {
    QJSON(value)(newFormats)
  }

}

object QJSON extends ToParameter with QJSONImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case j: JValue => JSONToParameterValue(j)(DefaultFormats)
  }
}

trait QJSONImplicits {
  implicit def JSONToParameterValue(x: JValue)(implicit formats: Formats): ParameterValue[JValue] = {
    QJSON(x)
  }
}

case class QLTree(value: LTree) extends ParameterValue[LTree] {

  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setObject(parameterIndex, value)
  }

}

object QLTree extends ToParameter with QLTreeImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: LTree => l
  }
}

trait QLTreeImplicits {
  implicit def LTreeToParameterValue(x: LTree): ParameterValue[LTree] = {
    QLTree(x)
  }
}

case class QXML(value: Node) extends ParameterValue[Node] {

  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    val sqlxml = preparedStatement.getConnection.createSQLXML()
    sqlxml.setString(value.toString)
    preparedStatement.setSQLXML(parameterIndex, sqlxml)
  }

}

object QXML extends ToParameter with QXMLImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case x: Node => x
  }
}

trait QXMLImplicits {
  implicit def XMLToParameterValue(x: Node): ParameterValue[Node] = {
    QXML(x)
  }
}
