package com.wda.sdbc
package postgresql

import java.net.InetAddress
import java.sql.SQLDataException
import java.util.UUID

import com.wda.sdbc.base._
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.{XML, Node}

trait Getters extends DefaultGetters {
  self: Row with Getter =>

  implicit val LTreeGetter = new Getter[LTree] {
    override def apply(row: Row, columnIndex: Int): Option[LTree] = {
      Option(row.getObject(columnIndex)) collect {
        case l: LTree => l
        case _ => throw new SQLDataException("column does not contain an LTree value")
      }
    }
  }

  implicit val PGIntervalGetter = new Getter[PGInterval] {
    override def apply(row: Row, columnIndex: Int): Option[PGInterval] = {
      Option(row.getObject(columnIndex)).collect {
        case pgInterval: PGInterval => pgInterval
        case _ => throw new SQLDataException("column does not contain a PGInterval")
      }
    }
  }

  implicit val InetAddressGetter = new Getter[InetAddress] {
    override def apply(row: Row, columnIndex: Int): Option[InetAddress] = {
      row.option[String](columnIndex).map(InetAddress.getByName)
    }
  }

  implicit val JValueGetter = new Parser[JValue] {
    override def parse(asString: String): JValue = {
      JsonMethods.parse(asString)
    }
  }

  override implicit val UUIDGetter: Getter[UUID] = new Getter[UUID] {
    override def apply(row: Row, columnIndex: Int): Option[UUID] = {
      Option(row.getObject(columnIndex)).collect {
        case uuid: UUID => uuid
        case _ => throw new SQLDataException("column does not contain a UUID")
      }
    }
  }

  implicit val XMLGetter: Getter[Node] = new Parser[Node] {
    //PostgreSQL's ResultSet#getSQLXML just uses getString.
    override def parse(asString: String): Node = {
      XML.loadString(asString)
    }
  }

}
