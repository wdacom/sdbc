package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.sql.PreparedStatement
import java.time.{LocalTime, OffsetDateTime}
import java.util.UUID

import com.rocketfuel.sdbc.base.{ToParameter, jdbc}
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId

import scala.xml.Node

trait QLocalTimeImplicits {
  implicit val LocalTimeIsParameter: IsParameter[LocalTime] = new IsParameter[LocalTime] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: LocalTime): Unit = {
      preparedStatement.setString(parameterIndex, parameter.toString)
    }
  }

  implicit def LocalTimeToParameterValue(l: LocalTime): ParameterValue = {
    ParameterValue(l)
  }
}

object QLocalTime extends ToParameter {
  override val toParameter: PartialFunction[Any, Any] = {
    case o: LocalTime =>
      o.toString
  }
}

object QOffsetDateTime extends ToParameter {
  override val toParameter: PartialFunction[Any, Any] = {
    case o: OffsetDateTime =>
      offsetDateTimeFormatter.format(o)
  }
}

trait QOffsetDateTimeImplicits {
  implicit val OffsetDateTimeIsParameter: IsParameter[OffsetDateTime] = new IsParameter[OffsetDateTime] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: OffsetDateTime): Unit = {
      preparedStatement.setString(parameterIndex, offsetDateTimeFormatter.format(parameter))
    }
  }

  implicit def OffsetDateTimeToParameterValue(o: OffsetDateTime): ParameterValue = {
    ParameterValue(offsetDateTimeFormatter.format(o))
  }
}

object QUUID extends ToParameter {
  override val toParameter: PartialFunction[Any, Any] = {
    case u: UUID => u
  }
}

trait QUUIDImplicits {
  implicit val UUIDIsParameter: IsParameter[UUID] = new IsParameter[UUID] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: UUID): Unit = {
      preparedStatement.setString(parameterIndex, parameter.toString)
    }
  }

  implicit def UUIDToParameterValue(x: UUID): ParameterValue = {
    ParameterValue(x)
  }
}

object QHierarchyId extends ToParameter {
  override val toParameter: PartialFunction[Any, Any] = {
    case h: HierarchyId => h
  }
}

trait QHierarchyIdImplicits {
  implicit val HierarchyIdIsParameter: IsParameter[HierarchyId] = new IsParameter[HierarchyId] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: HierarchyId): Unit = {
      preparedStatement.setString(parameterIndex, parameter.toString)
    }
  }

  implicit def HierarchyIdToParameterValue(x: HierarchyId): ParameterValue = {
    ParameterValue(x)
  }
}

object QXML extends ToParameter with QXMLImplicits {
  override val toParameter: PartialFunction[Any, Any] = {
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
