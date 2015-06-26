package com.wda.sdbc
package sqlserver

import java.sql.PreparedStatement
import java.util.UUID

import com.wda.sdbc.base._

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
  with LocalDateTimeParameter
  with DateTimeParameter {
  self: ParameterValue with Row with HierarchyId with HasDateTimeFormatter =>

  implicit class QUUID(override val value: UUID) extends ParameterValue[UUID] {
    override def asJDBCObject: AnyRef = value.toString

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateString(columnIndex, value.toString)
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.toString)
    }
  }

  implicit class QHierarchyId(override val value: HierarchyId) extends ParameterValue[HierarchyId] {
    override def asJDBCObject: AnyRef = value.toString

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.toString)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateString(columnIndex, value.toString)
    }
  }

  implicit class QXML(override val value: Node) extends ParameterValue[Node] {
    override def asJDBCObject: AnyRef = value.toString

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.toString)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateString(columnIndex, value.toString)
    }
  }

}
