package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.sql.PreparedStatement
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.sqlserver.jdbc.implementation
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId

import scala.xml.Node

object QUUID extends ToParameter with QUUIDImplicits {
  implicit val UUIDIsParameter: IsParameter[UUID] = new IsParameter[UUID] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: UUID): Unit = {
      preparedStatement.setString(parameterIndex, parameter.toString)
    }
  }
  
  override val toParameter: PartialFunction[Any, AnyParameter] = {
    case u: UUID => u
  }
}

trait QUUIDImplicits {
  implicit def UUIDToParameterValue(x: UUID): ParameterValue[UUID] = {
    jdbc.QUUID(x)
  }
}

case class QHierarchyId(value: HierarchyId) extends ParameterValue[HierarchyId]

object QHierarchyId extends ToParameter with QHierarchyIdImplicits {
  override val toParameter: PartialFunction[Any, AnyParameter] = {
    case h: HierarchyId => h
  }
}

trait QHierarchyIdImplicits {
  implicit val HierarchyIdIsParameter: IsParameter[HierarchyId] = new IsParameter[HierarchyId] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: HierarchyId): Unit = {
      preparedStatement.setString(parameterIndex, parameter.toString)
    }
  }
  
  implicit def HierarchyIdToParameterValue(x: HierarchyId): ParameterValue[HierarchyId] = {
    QHierarchyId(x)
  }
}

object QXML extends ToParameter with QXMLImplicits {
  override val toParameter: PartialFunction[Any, AnyParameter] = {
    case x: Node => x
  }
}

trait QXMLImplicits extends jdbc.QXMLImplicits {
  override implicit val NodeIsParameter: IsParameter[Node] = new IsParameter[Node] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: Node): Unit = {
      preparedStatement.setString(parameterIndex, parameter.toString)
    }
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

  type QUUID = jdbc.QUUID
  val QUUID = implementation.QUUID

  type QHierarchyId = implementation.QHierarchyId
  val QHierarchyId = implementation.QHierarchyId

  type QXML = jdbc.QXML
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
