package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.{SQLException, SQLDataException}
import java.time.format.DateTimeFormatter
import java.time.{Duration => JavaDuration, _}
import java.util.UUID
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc.{Cidr, LTree}
import org.json4s.JValue
import org.postgresql.util.{PGInterval, PGobject}
import scala.xml.{Node, XML}
import scala.concurrent.duration.{Duration => ScalaDuration}

//PostgreSQL doesn't support Byte, so we don't use the default getters.
private[sdbc] trait Getters
  extends BooleanGetter
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
  with LocalDateTimeGetter  {
  self: PGTimestampTzImplicits
    with PGTimeTzImplicits
    with IntervalImplicits
    with PGInetAddressImplicits
    with PGJsonImplicits =>

  implicit val LTreeGetter = new Getter[LTree] {
    override def apply(row: Row, ix: Index): Option[LTree] = {
      Option(row.getObject(ix(row))).map {
        case l: LTree => l
        case _ => throw new SQLException("column does not contain an ltree")
      }
    }
  }

  implicit val PGIntervalGetter = new Getter[PGInterval] {
    override def apply(row: Row, ix: Index): Option[PGInterval] = {
      Option(row.getObject(ix(row))).map {
        case p: PGInterval => p
        case _ => throw new SQLException("column does not contain an interval")
      }
    }
  }

  implicit val CidrGetter = new Getter[Cidr] {
    override def apply(row: Row, ix: Index): Option[Cidr] = {
      Option(row.getObject(ix(row))).map {
        case p: Cidr => p
        case _ => throw new SQLException("column does not contain a cidr")
      }
    }
  }

  private def IsPGobjectGetter[T](implicit converter: PGobject => T): Getter[T] = new Getter[T] {
    override def apply(row: Row, ix: Index): Option[T] = {
      val shouldBePgValue = Option(row.getObject(ix(row)))
      shouldBePgValue.map {
        case p: PGobject =>
          converter(p)
        case _ =>
          throw new SQLException("column does not contain a PGobject")
      }
    }
  }

  implicit val ScalaDurationGetter = IsPGobjectGetter[ScalaDuration]

  implicit val JavaDurationGetter = IsPGobjectGetter[JavaDuration]

  implicit val JValueGetter = IsPGobjectGetter[JValue]

  implicit val InetAddressGetter = IsPGobjectGetter[InetAddress]

  implicit val OffsetTimeGetter = new Getter[OffsetTime] {
    override def apply(row: Row, ix: Index): Option[OffsetTime] = {
      for {
        asString <- Option(row.getString(ix(row)))
      } yield {
        val parsed = offsetTimeFormatter.parse(asString)
        OffsetTime.from(parsed)
      }
    }
  }

  implicit val OffsetDateTimeGetter = new Getter[OffsetDateTime] {
    override def apply(row: Row, ix: Index): Option[OffsetDateTime] = {
      for {
        asString <- Option(row.getString(ix(row)))
      } yield {
        val parsed = offsetDateTimeFormatter.parse(asString)
        OffsetDateTime.from(parsed)
      }
    }
  }

  implicit val LocalTimeGetter = new Getter[LocalTime] {
    override def apply(row: Row, ix: Index): Option[LocalTime] = {
      for {
        asString <- Option(row.getString(ix(row)))
      } yield {
        val parsed = DateTimeFormatter.ISO_LOCAL_TIME.parse(asString)
        val l = LocalTime.from(parsed)
        l
      }
    }
  }

  override implicit val UUIDGetter: Getter[UUID] = new Getter[UUID] {
    override def apply(row: Row, ix: Index): Option[UUID] = {
      Option(row.getObject(ix(row))).map {
        case uuid: UUID => uuid
        case _ => throw new SQLDataException("column does not contain a uuid")
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

}
