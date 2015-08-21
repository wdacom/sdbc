package com.rocketfuel.sdbc.h2.jdbc.implementation

import java.sql.{Time, Date}
import java.time.{OffsetDateTime, LocalDateTime, Instant}
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc

import scala.reflect.runtime.universe._

trait ArrayParameter extends jdbc.ArrayParameter {
  self: H2Common =>

  override def typeName(tpe: Type): String = {
    tpe match {
      case t if t =:= typeOf[Short] => "SMALLINT"
      case t if t =:= typeOf[Int] => "INTEGER"
      case t if t =:= typeOf[Long] => "BIGINT"
      case t if t =:= typeOf[Float] => "REAL"
      case t if t =:= typeOf[Double] => "DOUBLE"
      case t if t =:= typeOf[java.lang.Short] => "SMALLINT"
      case t if t =:= typeOf[java.lang.Integer] => "INTEGER"
      case t if t =:= typeOf[java.lang.Long] => "BIGINT"
      case t if t =:= typeOf[java.lang.Float] => "REAL"
      case t if t =:= typeOf[java.lang.Double] => "DOUBLE"
      case t if t =:= typeOf[BigDecimal] => "DECIMAL"
      case t if t =:= typeOf[java.math.BigDecimal] => "DECIMAL"
      case t if t =:= typeOf[LocalDateTime] => "TIMESTAMP"
      case t if t =:= typeOf[OffsetDateTime] => "TIMESTAMP"
      case t if t =:= typeOf[Instant] => "TIMESTAMP"
      case t if t =:= typeOf[Date] => "DATE"
      case t if t =:= typeOf[Time] => "TIME"
      case t if t =:= typeOf[java.time.LocalDate] => "DATE"
      case t if t =:= typeOf[java.time.LocalTime] => "TIME"
      case t if t =:= typeOf[Boolean] => "BOOLEAN"
      case t if t =:= typeOf[java.lang.Boolean] => "BOOLEAN"
      case t if t =:= typeOf[String] => "VARCHAR"
      case t if t =:= typeOf[UUID] => "UUID"
      case t if t <:< typeOf[QArray[_]] =>
        innerTypeName(t)
      case t if t <:< typeOf[Seq[_]] =>
        innerTypeName(t)
      case t => throw new Exception("H2 does not understand " + t.toString)
    }
  }

}
