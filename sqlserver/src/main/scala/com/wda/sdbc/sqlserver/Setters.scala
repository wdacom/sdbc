package com.wda.sdbc
package sqlserver

import java.sql.PreparedStatement

import com.wda.sdbc.base.{Java8DefaultSetters, Row, ParameterValue}

import scala.xml.Elem

trait Setters
  extends Java8DefaultSetters {
  self: ParameterValue with Row with HierarchyId =>

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

  implicit class QXML(override val value: Elem) extends ParameterValue[Elem] {
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
