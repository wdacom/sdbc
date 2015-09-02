package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.{Date, Time}
import java.time.{Duration, LocalDateTime, OffsetDateTime, OffsetTime}
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.base.jdbc.Index
import com.rocketfuel.sdbc.postgresql.jdbc.{Cidr, LTree}
import org.json4s._

import scala.reflect.runtime.universe._

trait SeqParameter extends jdbc.SeqParameter {
  self: PostgreSqlCommon =>

  def typeName(tpe: Type): String = {
    tpe match {
      case t if t =:= typeOf[Short] => "int2"
      case t if t =:= typeOf[Int] => "int4"
      case t if t =:= typeOf[Long] => "int8"
      case t if t =:= typeOf[Float] => "float4"
      case t if t =:= typeOf[Double] => "float8"
      case t if t =:= typeOf[java.lang.Short] => "int2"
      case t if t =:= typeOf[java.lang.Integer] => "int4"
      case t if t =:= typeOf[java.lang.Long] => "int8"
      case t if t =:= typeOf[java.lang.Float] => "float4"
      case t if t =:= typeOf[java.lang.Double] => "float8"
      case t if t =:= typeOf[BigDecimal] => "numeric"
      case t if t =:= typeOf[java.math.BigDecimal] => "numeric"
      case t if t =:= typeOf[LocalDateTime] => "timestamp"
      case t if t =:= typeOf[OffsetDateTime] => "timestamptz"
      case t if t =:= typeOf[Date] => "date"
      case t if t =:= typeOf[Time] => "time"
      case t if t =:= typeOf[java.time.LocalDate] => "date"
      case t if t =:= typeOf[java.time.LocalTime] => "time"
      case t if t =:= typeOf[OffsetTime] => "timetz"
      case t if t =:= typeOf[Duration] => "interval"
      case t if t =:= typeOf[Boolean] => "boolean"
      case t if t =:= typeOf[java.lang.Boolean] => "boolean"
      case t if t =:= typeOf[String] => "text"
      case t if t =:= typeOf[scala.xml.Elem] => "xml"
      case t if t <:< typeOf[JValue] => "json"
      case t if t =:= typeOf[LTree] => "ltree"
      case t if t =:= typeOf[UUID] => "uuid"
      case t if t =:= typeOf[InetAddress] => "inet"
      case t if t =:= typeOf[Cidr] => "cidr"
      case t if t =:= typeOf[Map[String, String]] => "hstore"
      case t if t <:< typeOf[QSeq[_]] =>
        innerTypeName(t)
      case t if t <:< typeOf[Seq[_]] =>
        innerTypeName(t)
      case t => throw new Exception("PostgreSQL does not understand " + t.toString)
    }
  }

  //Override what would be the inferred Seq[Byte] getter, because you can't use ResultSet#getArray
  //to get the bytes.
  implicit val SeqByteGetter = new Getter[Seq[Byte]] {
    override def apply(row: Row, ix: Index): Option[Seq[Byte]] = {
      Option(row.getBytes(ix(row))).map(_.toSeq)
    }
  }

}
