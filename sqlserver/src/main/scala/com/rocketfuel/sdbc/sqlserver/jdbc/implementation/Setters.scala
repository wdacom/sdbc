package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.sql.PreparedStatement
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId

import scala.xml.Node

//We have to use a special UUID getter, so we can't use the default setters.
trait Setters
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
  with InstantParameter
  with LocalDateParameter
  with LocalTimeParameter
  with LocalDateTimeParameter
  with OffsetDateTimeParameter
  with OffsetTimeParameter {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  case class QUUID(value: UUID) extends ParameterValue[UUID] {
    override def set(statement: PreparedStatement, parameterIndex: Int): Unit = {
      statement.setString(parameterIndex, value.toString)
    }
  }

  implicit def UUIDToParameterValue(x: UUID): ParameterValue[UUID] = {
    QUUID(x)
  }

  case class QHierarchyId(value: HierarchyId) extends ParameterValue[HierarchyId] {
    override def set(statement: PreparedStatement, parameterIndex: Int): Unit = {
      statement.setString(parameterIndex, value.toString)
    }
  }

  implicit def HierarchyIdToParameterValue(x: HierarchyId): ParameterValue[HierarchyId] = {
    QHierarchyId(x)
  }

  implicit class QXML(override val value: Node) extends ParameterValue[Node] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.toString)
    }
  }

  implicit def XMLToParameterValue(x: Node): ParameterValue[Node] = {
    QXML(x)
  }

}
