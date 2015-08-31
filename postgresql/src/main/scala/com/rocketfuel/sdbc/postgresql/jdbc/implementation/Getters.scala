package com.rocketfuel.sdbc.postgresql.jdbc.implementation


import java.net.InetAddress
import java.sql.{SQLException, SQLDataException}
import java.util.UUID
import java.util.concurrent.TimeUnit

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc.LTree
import org.json4s.JValue
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import java.time.{Duration => JavaDuration, OffsetTime, OffsetDateTime}
import scala.concurrent.duration.{Duration => ScalaDuration}
import scala.xml.{Node, XML}

//PostgreSQL doesn't support Byte, so we don't use the default getters.
trait Getters extends AnyRefGetter
  with BooleanGetter
  with BytesGetter
  with DateGetter
  with DoubleGetter
  with FloatGetter
  with InputStreamGetter
  with IntGetter
  with JavaBigDecimalGetter
  with LongGetter
  with ReaderGetter
  with ScalaBigDecimalGetter
  with ShortGetter
  with StringGetter
  with TimeGetter
  with TimestampGetter
  with UUIDGetter
  with ParameterGetter
  with InstantGetter
  with LocalDateGetter
  with LocalDateTimeGetter
  with LocalTimeGetter
  with IntervalImplicits {

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

  implicit val JavaDurationGetter = new Getter[JavaDuration] {
    override def apply(row: Row, ix: Index): Option[JavaDuration] = {
      PGIntervalGetter(row, ix).map(PGIntervalToJavaDuration)
    }
  }

  implicit val ScalaDurationGetter = new Getter[ScalaDuration] {
    override def apply(
      row: Row,
      ix: Index
    ): Option[ScalaDuration] = {
      PGIntervalGetter(row, ix).map(PGIntervalToDuration)
    }
  }

  implicit val JValueGetter = new Getter[JValue] {
    override def apply(row: Row, ix: Index): Option[JValue] = {
      Option(row.getObject(ix(row))).map {
        case asPg: PGJson =>
          asPg.value.get
        case _ =>
          throw new SQLException("column does not contain a ")
      }
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

  implicit val XMLGetter: Getter[Node] = new Parser[Node] {
    //PostgreSQL's ResultSet#getSQLXML just uses getString.
    override def parse(asString: String): Node = {
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

  implicit val InetAddressGetter: Getter[InetAddress] = new Getter[InetAddress] {
    override def apply(row: Row, ix: Index): Option[InetAddress] = {
      Option(row.getObject(ix(row))).map {
        case p: PGInetAddress =>
          p.inetAddress.get
      }
    }
  }

  implicit val OffsetDateTimeGetter: Getter[OffsetDateTime] = new Getter[OffsetDateTime] {
    override def apply(row: Row, ix: Index): Option[OffsetDateTime] = {
      Option(row.getObject(ix(row))).map {
        case p: PGTimestampTz => OffsetDateTime.from(offsetDateTimeFormatter.parse(p.getValue))
      }
    }
  }

  implicit val OffsetTimeGetter: Getter[OffsetTime] = new Getter[OffsetTime] {
    override def apply(row: Row, ix: Index): Option[OffsetTime] = {
      Option(row.getObject(ix(row))).map {
        case p: PGTimeTz => OffsetTime.from(offsetTimeFormatter.parse(p.getValue))
      }
    }
  }

}
