package com.wda.sdbc.postgresql

import java.net.InetAddress
import java.sql._
import java.time.{Duration, LocalDateTime, OffsetDateTime, OffsetTime}
import java.util.UUID

import com.wda.sdbc.base._
import org.json4s.JValue

import scala.collection.immutable.Seq
import scala.reflect.runtime.universe._

abstract class PostgreSql
  extends PostgreSqlCommon
  with SeqParameterValue {

  override def typeName[T](implicit tag: TypeTag[T]): String = {
    typeName(tag.tpe)
  }

  def innerTypeName(tpe: Type): String = {
    val innerType = tpe.dealias.typeArgs.head.dealias
    typeName(innerType)
  }

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
      case t if t <:< typeOf[QArray[Any]] =>
        innerTypeName(t)
      case t if t <:< typeOf[Seq[_]] =>
        innerTypeName(t)
      case t => throw new Exception("PostgreSQL does not understand " + t.toString)
    }
  }

}
