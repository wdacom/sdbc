package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.sql.PreparedStatement
import java.util.UUID

import com.rocketfuel.sdbc.sqlserver.jdbc.implementation
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId

import scala.xml.Node

case class QUUID(value: UUID) extends ParameterValue[UUID] {
  override def set(statement: PreparedStatement, parameterIndex: Int): Unit = {
    statement.setString(parameterIndex, value.toString)
  }
}

object QUUID extends ToParameter with QUUIDImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case u: UUID => u
  }
}

trait QUUIDImplicits {
  implicit def UUIDToParameterValue(x: UUID): ParameterValue[UUID] = {
    QUUID(x)
  }
}

case class QHierarchyId(value: HierarchyId) extends ParameterValue[HierarchyId] {
  override def set(statement: PreparedStatement, parameterIndex: Int): Unit = {
    statement.setString(parameterIndex, value.toString)
  }
}

object QHierarchyId extends ToParameter with QHierarchyIdImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case h: HierarchyId => h
  }
}

trait QHierarchyIdImplicits {
  implicit def HierarchyIdToParameterValue(x: HierarchyId): ParameterValue[HierarchyId] = {
    QHierarchyId(x)
  }
}

case class QXML(override val value: Node) extends ParameterValue[Node] {
  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setString(parameterIndex, value.toString)
  }
}

object QXML extends ToParameter with QXMLImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case x: Node => XMLToParameterValue(x)
  }
}

trait QXMLImplicits {
  implicit def XMLToParameterValue(x: Node): ParameterValue[Node] = {
    QXML(x)
  }
}

//We have to use a special UUID getter, so we can't use the default setters.
trait Setters
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
  with QInstantImplicits
  with QLocalDateImplicits
  with QLocalTimeImplicits
  with QLocalDateTimeImplicits
  with QOffsetDateTimeImplicits
  with QUUIDImplicits
  with QHierarchyIdImplicits
  with QXMLImplicits {
  self: HasOffsetDateTimeFormatter =>

  type QUUID = implementation.QUUID
  val QUUID = implementation.QUUID

  type QHierarchyId = implementation.QHierarchyId
  val QHierarchyId = implementation.QHierarchyId

  type QXML = implementation.QXML
  val QXML = implementation.QXML

  val toSqlServerParameter =
    QBoolean.toParameter orElse
      QByte.toParameter orElse
      QBytes.toParameter orElse
      QDate.toParameter orElse
      QBigDecimal.toParameter orElse
      QDouble.toParameter orElse
      QFloat.toParameter orElse
      QInt.toParameter orElse
      QLong.toParameter orElse
      QShort.toParameter orElse
      QString.toParameter orElse
      QTime.toParameter orElse
      QTimestamp.toParameter orElse
      QReader.toParameter orElse
      QInputStream.toParameter orElse
      QInstant.toParameter orElse
      QLocalDate.toParameter orElse
      QLocalTime.toParameter orElse
      QLocalDateTime.toParameter orElse
      QOffsetDateTime.toParameter orElse
      QUUID.toParameter orElse
      QHierarchyId.toParameter orElse
      QXML.toParameter

}
