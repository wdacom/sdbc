package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.PreparedStatement

import com.rocketfuel.sdbc.base.jdbc.{Java8DefaultSetters, ParameterValue}
import com.rocketfuel.sdbc.postgresql.jdbc.LTree
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.Elem

trait Setters
  extends Java8DefaultSetters {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  case class QInetAddress(value: InetAddress) extends ParameterValue[InetAddress] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.getHostAddress)
    }

  }

  def InetAddressToParameterValue(x: InetAddress): ParameterValue[InetAddress] = {
    QInetAddress(x)
  }

  case class QPGInterval(value: PGInterval) extends ParameterValue[PGInterval] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

  def PGIntervalToParameterValue(x: PGInterval): ParameterValue[PGInterval] = {
    QPGInterval(x)
  }

  case class QJSON(value: JValue)(implicit formats: Formats) extends ParameterValue[JValue] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, JsonMethods.compact(JsonMethods.render(value)))
    }

  }

  def JSONToParameterValue(x: JValue)(implicit formats: Formats): ParameterValue[JValue] = {
    QJSON(x)
  }

  case class QLTree(value: LTree) extends ParameterValue[LTree] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }

  }

  def LTreeToParameterValue(x: LTree): ParameterValue[LTree] = {
    QLTree(x)
  }

  case class QXML(value: Elem) extends ParameterValue[Elem] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      val sqlxml = preparedStatement.getConnection.createSQLXML()
      sqlxml.setString(value.toString)
      preparedStatement.setSQLXML(parameterIndex, sqlxml)
    }

  }

  def XMLToParameterValue(x: Elem): ParameterValue[Elem] = {
    QXML(x)
  }

}
