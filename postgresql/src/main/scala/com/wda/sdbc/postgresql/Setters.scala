package com.wda.sdbc
package postgresql

import java.net.InetAddress
import java.sql.PreparedStatement

import com.wda.sdbc.base._
import org.joda.time.Duration
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.Node

trait Setters
  extends DefaultSetters
  with DateTimeParameterAsTimestamp {
  self: ParameterValue with Row with HasDateTimeFormatter with DurationImplicits =>

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

  implicit class QDuration(override val value: Duration) extends ParameterValue[Duration] {
    val asPGInterval: PGInterval = value

    override def asJDBCObject: AnyRef = asPGInterval

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateObject(columnIndex, asPGInterval)
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setObject(parameterIndex, asPGInterval)
    }
  }

  implicit class QJSON(override val value: JValue)(implicit formats: Formats) extends ParameterValue[JValue] {
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

  implicit class QHStore(override val value: Map[String, String]) extends ParameterValue[Map[String, String]] {
    import scala.collection.convert.decorateAsJava._

    val asJavaMap = value.asJava

    override def asJDBCObject: AnyRef = asJavaMap

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, asJavaMap)
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJavaMap)
    }
  }

}
