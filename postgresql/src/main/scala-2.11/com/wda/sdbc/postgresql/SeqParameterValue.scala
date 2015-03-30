package com.wda.sdbc.postgresql

import java.net.InetAddress
import java.sql.{Time, Date, PreparedStatement}
import java.time.{Duration, OffsetTime, OffsetDateTime, LocalDateTime}
import java.util.UUID

import com.wda.sdbc.base.{ParameterValue, Row}
import org.json4s._

import scala.collection.immutable.Seq
import scala.reflect.runtime.universe._

trait SeqParameterValue {
  self: Row with ParameterValue =>

  def typeName[T](implicit tag: TypeTag[T]): String = {
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

  case class QArray[T](
    override val value: Seq[Option[ParameterValue[T]]]
    )(implicit t: TypeTag[T]
    ) extends ParameterValue[Seq[Option[ParameterValue[T]]]] {
    /**
     * Get the values of this array in a form suitable for use
     * with Java methods that expect an Array.
     * @return
     */
    def asJava: Array[AnyRef] = {
      value.map(_.map(v => v.asJDBCObject).orNull).toArray
    }

    override def asJDBCObject: AnyRef = asJava

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      val array = preparedStatement.getConnection.createArrayOf(typeName[T], asJava)
      preparedStatement.setArray(parameterIndex, array)
    }

    override def update(row: Row, columnIndex: Int): Unit = {
      throw new NotImplementedError("Creating a JDBC array requires access to the Connection object, which Row doesn't have a pointer to.")
    }
  }

  implicit def SeqToOptionParameterValue[T, S](
    v: Seq[T]
  )(implicit conversion: T => ParameterValue[S],
    ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    Some(QArray(v.map(conversion andThen Some.apply)))
  }

  implicit def OptionSeqToOptionParameterValue[T, S](
    vOpt: Option[Seq[T]]
  )(implicit conversion: T => ParameterValue[S], ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    vOpt.map(v => QArray(v.map(conversion andThen Some.apply)))
  }

  implicit def SeqOptionToOptionParameterValue[T, S](
    v: Seq[Option[T]]
  )(implicit conversion: T => ParameterValue[S],
    ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    Some(QArray(v.map(_.map(conversion))))
  }

  implicit def OptionOptionSeqToOptionParameterValue[T, S](
    vOpt: Option[Seq[Option[T]]]
  )(implicit conversion: T => ParameterValue[S],
    ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    vOpt.map(v => QArray(v.map(_.map(conversion))))
  }

}
