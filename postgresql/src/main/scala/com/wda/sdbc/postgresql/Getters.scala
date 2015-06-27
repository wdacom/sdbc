package com.wda.sdbc
package postgresql

import java.net.InetAddress
import java.sql.SQLException
import java.util.UUID

import com.wda.sdbc.base._
import org.joda.time.Duration
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.{XML, Node}

trait Getters extends DefaultGetters with DateTimeGetter {
  self: Row with Getter with DurationImplicits with HasDateTimeFormatter =>

  implicit val LTreeGetter = new Getter[LTree] {
    override def apply(row: Row, columnIndex: Int): Option[LTree] = {
      Option(row.getObject(columnIndex)) collect {
        case l: LTree => l
        case _ => throw new SQLException("column does not contain an LTree value")
      }
    }
  }

  implicit val PGIntervalGetter = new Getter[PGInterval] {
    override def apply(row: Row, columnIndex: Int): Option[PGInterval] = {
      Option(row.getObject(columnIndex)).collect {
        case pgInterval: PGInterval => pgInterval
        case _ => throw new SQLException("column does not contain a PGInterval")
      }
    }
  }

  implicit val DurationGetter = new Getter[Duration] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Duration] = {
      row.option[PGInterval](columnIndex).map {
        pgInterval =>
          val asDuration: Duration = pgInterval
          asDuration
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
        case _ => throw new SQLException("column does not contain a UUID")
      }
    }
  }

  implicit val XMLGetter: Getter[Node] = new Parser[Node] {
    //PostgreSQL's ResultSet#getSQLXML just uses getString.
    override def parse(asString: String): Node = {
      XML.loadString(asString)
    }
  }

  implicit val HStoreGetter: Getter[Map[String, String]] = new Getter[Map[String, String]] {
    override def apply(row: Row, columnIndex: Int): Option[Map[String, String]] = {
      Option(row.getObject(columnIndex)).map {
        case m: java.util.Map[_, _] =>
          import scala.collection.convert.wrapAsScala._
          m.toMap.map {
            case (k: String, v: String) =>
              (k, v)
            case _ =>
              throw new SQLException("Map contained a key or value that is not a String.")
          }
        case otherwise =>
          throw new SQLException(s"Expected an instance of java.util.Map[String, String], but got ${otherwise.getClass.getName}.")
      }
    }
  }

}
