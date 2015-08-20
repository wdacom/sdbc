package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.{SQLDataException, SQLException}
import org.joda.time.{Duration => JodaDuration}
import scala.concurrent.duration.{Duration => ScalaDuration}
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc._
import org.json4s._
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.{Elem, XML}

trait Getters extends DefaultGetters with DateTimeGetter {
  self: PgDateTimeFormatter =>

  implicit val LTreeGetter = new Getter[LTree] {
    override def apply(row: Row, ix: Index): Option[LTree] = {
      Option(row.getObject(ix(row))).map {
        case l: LTree => l
        case _ => throw new SQLDataException("column does not contain an LTree value")
      }
    }
  }

  implicit val PGIntervalGetter = new Getter[PGInterval] {
    override def apply(row: Row, ix: Index): Option[PGInterval] = {
      Option(row.getObject(ix(row))).map {
        case pgInterval: PGInterval => pgInterval
        case _ => throw new SQLDataException("column does not contain a PGInterval")
      }
    }
  }

  implicit val JodaDurationGetter = new Getter[JodaDuration] {
    override def apply(row: Row, ix: Index): Option[JodaDuration] = {
      PGIntervalGetter(row, ix).map(interval => interval)
    }
  }

  implicit val ScalaDurationGetter = new Getter[ScalaDuration] {
    override def apply(row: Row, ix: Index): Option[ScalaDuration] = {
      PGIntervalGetter(row, ix).map(interval => interval)
    }
  }

  implicit val InetAddressGetter = new Parser[InetAddress] {
    override def parse(asString: String): InetAddress = {
      InetAddress.getByName(asString)
    }
  }

  implicit val JValueGetter = new Parser[JValue] {
    override def parse(asString: String): JValue = {
      JsonMethods.parse(asString)
    }
  }

  override implicit val UUIDGetter: Getter[UUID] = new Getter[UUID] {
    override def apply(row: Row, ix: Index): Option[UUID] = {
      Option(row.getObject(ix(row))).map {
        case uuid: UUID => uuid
        case _ => throw new SQLDataException("column does not contain a UUID")
      }
    }
  }

  implicit val XMLGetter: Getter[Elem] = new Parser[Elem] {
    //PostgreSQL's ResultSet#getSQLXML just uses getString.
    override def parse(asString: String): Elem = {
      XML.loadString(asString)
    }
  }

  implicit val MapGetter: Getter[Map[String, String]] = new Getter[Map[String, String]] {
    override def apply(row: Row, ix: Index): Option[Map[String, String]] = {
      Option(row.getObject(ix(row))).map {
        case m: java.util.Map[_, _] =>
          import scala.collection.convert.decorateAsScala._
          val values =
            for (entry <- m.entrySet().asScala) yield {
              entry.getKey.asInstanceOf[String] -> entry.getValue.asInstanceOf[String]
            }
          Map(values.toSeq: _*)
        case _ =>
          throw new SQLException("column does not contain an hstore")
      }
    }
  }

}
