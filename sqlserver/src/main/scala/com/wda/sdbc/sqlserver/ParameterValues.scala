package com.wda.sdbc
package sqlserver

import java.sql.PreparedStatement
import java.util.UUID

import com.wda.sdbc.base.{Row, ParameterValue}

import scala.xml.Elem

trait ParameterValues {
  self: ParameterValue with Row with HierarchyId =>

  case class QHierarchyId(value: HierarchyId) extends ParameterValue[HierarchyId] {
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

  case class QUUID(value: UUID) extends ParameterValue[UUID] {
    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.toString)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateObject(columnIndex, asJDBCObject)
    }
  }


  case class QXML(value: Elem) extends ParameterValue[Elem] {
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
