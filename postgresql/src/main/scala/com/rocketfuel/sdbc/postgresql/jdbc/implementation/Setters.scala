package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.PreparedStatement

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc.LTree
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.{Node, Elem}

trait Setters
  extends Java8DefaultSetters {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  case class QInetAddress(value: InetAddress) extends ParameterValue[InetAddress] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, value.getHostAddress)
    }

  }

  object QInetAddress extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case i: InetAddress => i
    }
  }

  implicit def InetAddressToParameterValue(x: InetAddress): ParameterValue[InetAddress] = {
    QInetAddress(x)
  }

  case class QPGInterval(value: PGInterval) extends ParameterValue[PGInterval] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

  object QPGInterval extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case p: PGInterval => p
    }
  }

  implicit def PGIntervalToParameterValue(x: PGInterval): ParameterValue[PGInterval] = {
    QPGInterval(x)
  }

  case class QJSON(value: JValue)(implicit formats: Formats) extends ParameterValue[JValue] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, JsonMethods.compact(JsonMethods.render(value)))
    }

    def withFormats(implicit newFormats: Formats): QJSON = {
      QJSON(value)(newFormats)
    }

  }

  object QJSON extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case j: JValue => JSONToParameterValue(j)(DefaultFormats)
    }
  }

  implicit def JSONToParameterValue(x: JValue)(implicit formats: Formats): ParameterValue[JValue] = {
    QJSON(x)
  }

  case class QLTree(value: LTree) extends ParameterValue[LTree] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }

  }

  object QLTree extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case l: LTree => l
    }
  }

  implicit def LTreeToParameterValue(x: LTree): ParameterValue[LTree] = {
    QLTree(x)
  }

  case class QXML(value: Node) extends ParameterValue[Node] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      val sqlxml = preparedStatement.getConnection.createSQLXML()
      sqlxml.setString(value.toString)
      preparedStatement.setSQLXML(parameterIndex, sqlxml)
    }

  }

  object QXML extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case x: Node => x
    }
  }

  implicit def XMLToParameterValue(x: Node): ParameterValue[Node] = {
    QXML(x)
  }

  val toPostgresqlParameter: PartialFunction[Any, ParameterValue[_]] =
    toJava8DefaultParameter orElse
      QInetAddress.toParameter orElse
      QPGInterval.toParameter orElse
      QJSON.toParameter orElse
      QLTree.toParameter orElse
      QXML.toParameter

}
