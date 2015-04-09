package com.wda.sdbc
package postgresql

import java.net.InetAddress
import java.sql.PreparedStatement

import com.wda.sdbc.base.{Java8DefaultSetters, Row, ParameterValue}
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.Node

trait Setters
  extends Java8DefaultSetters {
  self: ParameterValue with Row =>

  implicit class QInetAddress(override val value: InetAddress) extends ParameterValue[InetAddress] {
    override def asJDBCObject: AnyRef = value.getHostAddress

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.getHostAddress)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateObject(columnIndex, asJDBCObject)
    }
  }

  implicit class QPGInterval(override val value: PGInterval) extends ParameterValue[PGInterval] {
    type T = PGInterval

    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJDBCObject)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateObject(columnIndex, asJDBCObject)
    }
  }

  implicit class QJSON(override val value: JValue)(implicit formats: Formats) extends ParameterValue[JValue] {
    type T = JValue

    override def asJDBCObject: AnyRef = JsonMethods.compact(JsonMethods.render(value))

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, JsonMethods.compact(JsonMethods.render(value)))
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateString(columnIndex, JsonMethods.compact(JsonMethods.render(value)))
    }
  }

  implicit class QLTree(override val value: LTree) extends ParameterValue[LTree] {
    type T = LTree

    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJDBCObject)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateObject(columnIndex, asJDBCObject)
    }
  }

  implicit class QXML(override val value: Node) extends ParameterValue[Node] {
    override def asJDBCObject: AnyRef = value.toString

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      val sqlxml = preparedStatement.getConnection.createSQLXML()
      sqlxml.setString(value.toString)
      preparedStatement.setSQLXML(parameterIndex, sqlxml)
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateString(columnIndex, value.toString)
    }
  }

}
