package com.wda.sdbc.cassandra

import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.{UUID, Date}
import scala.collection.convert.wrapAsJava._
import com.datastax.driver.core.{UDTValue, TupleValue, Token, BoundStatement}

trait ParameterValues {

  case class BooleanParameter(value: Boolean) extends ParameterValue[Boolean] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setBool(parameterIndex, value)
    }
  }

  implicit def BooleanToParameter(value: Boolean): ParameterValue[Boolean] = {
    BooleanParameter(value)
  }

  case class BoxedBooleanParameter(value: java.lang.Boolean) extends ParameterValue[java.lang.Boolean] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case null => statement.setToNull(parameterIndex)
        case _ => statement.setBool(parameterIndex, value)
      }
    }
  }

  implicit def BoxedBooleanToParameter(value: java.lang.Boolean): ParameterValue[java.lang.Boolean] = {
    BoxedBooleanToParameter(value)
  }

  case class ByteBufferParameter(value: ByteBuffer) extends ParameterValue[ByteBuffer] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setBytes(parameterIndex, value)
    }
  }

  implicit def ByteBufferToParameter(value: ByteBuffer): ParameterValue[ByteBuffer] = {
    ByteBufferParameter(value)
  }

  case class IterableByteParameter(value: Iterable[Byte]) extends ParameterValue[Iterable[Byte]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setBytes(parameterIndex, ByteBuffer.wrap(value.toArray))
    }
  }

  implicit def IterableByteToParameter(value: Iterable[Byte]): ParameterValue[Iterable[Byte]] = {
    IterableByteParameter(value)
  }

  case class DateParameter(value: Date) extends ParameterValue[Date] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDate(parameterIndex, value)
    }
  }

  implicit def DateToParameter(value: Date): ParameterValue[Date] = {
    DateParameter(value)
  }

  case class DecimalParameter(value: BigDecimal) extends ParameterValue[BigDecimal] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDecimal(parameterIndex, value.underlying())
    }
  }

  implicit def BigDecimalToParameter(value: BigDecimal): ParameterValue[BigDecimal] = {
    DecimalParameter(value)
  }

  case class JavaDecimalParameter(value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDecimal(parameterIndex, value)
    }
  }

  implicit def JavaDecimalToParameter(value: java.math.BigDecimal): ParameterValue[java.math.BigDecimal] = {
    JavaDecimalParameter(value)
  }

  case class DoubleParameter(value: Double) extends ParameterValue[Double] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDouble(parameterIndex, value)
    }
  }

  implicit def DoubleToParameter(value: Double): ParameterValue[Double] = {
    DoubleParameter(value)
  }

  case class BoxedDoubleParameter(value: java.lang.Double) extends ParameterValue[java.lang.Double] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case null => statement.setToNull(parameterIndex)
        case _ => statement.setDouble(parameterIndex, value)
      }
    }
  }

  implicit def BoxedDoubleToParameter(value: java.lang.Double): ParameterValue[java.lang.Double] = {
    BoxedDoubleParameter(value)
  }

  case class FloatParameter(value: Float) extends ParameterValue[Float] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setFloat(parameterIndex, value)
    }
  }

  implicit def FloatToParameter(value: Float): ParameterValue[Float] = {
    FloatParameter(value)
  }

  case class BoxedFloatParameter(value: java.lang.Float) extends ParameterValue[java.lang.Float] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case null => statement.setToNull(parameterIndex)
        case _ => statement.setFloat(parameterIndex, value)
      }
    }
  }

  implicit def BoxedFloatToParameter(value: java.lang.Float): ParameterValue[java.lang.Float] = {
    BoxedFloatParameter(value)
  }

  case class InetAddressParameter(value: InetAddress) extends ParameterValue[InetAddress] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setInet(parameterIndex, value)
    }
  }

  implicit def InetAddressToParameter(value: InetAddress): ParameterValue[InetAddress] = {
    InetAddressParameter(value)
  }

  case class IntParameter(value: Int) extends ParameterValue[Int] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setInt(parameterIndex, value)
    }
  }

  implicit def IntToParameter(value: Int): ParameterValue[Int] = {
    IntParameter(value)
  }

  case class BoxedIntParameter(value: java.lang.Integer) extends ParameterValue[java.lang.Integer] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case null => statement.setToNull(parameterIndex)
        case _ => statement.setInt(parameterIndex, value)
      }
    }
  }

  implicit def BoxedIntToParameter(value: java.lang.Integer): ParameterValue[java.lang.Integer] = {
    BoxedIntParameter(value)
  }

  case class SeqParameter[T](value: Seq[T]) extends ParameterValue[Seq[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setList[T](parameterIndex, value)
    }
  }

  implicit def SeqToParameter[T](value: Seq[T]): ParameterValue[Seq[T]] = {
    SeqParameter[T](value)
  }

  case class JavaListParameter[T](value: java.util.List[T]) extends ParameterValue[java.util.List[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setList[T](parameterIndex, value)
    }
  }

  implicit def JavaListToParameter[T](value: Seq[T]): ParameterValue[java.util.List[T]] = {
    JavaListParameter[T](value)
  }

  case class LongParameter(value: Long) extends ParameterValue[Long] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setLong(parameterIndex, value)
    }
  }

  implicit def LongToParameter(value: Long): ParameterValue[Long] = {
    LongParameter(value)
  }

  case class BoxedLongParameter(value: java.lang.Long) extends ParameterValue[java.lang.Long] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case null => statement.setToNull(parameterIndex)
        case _ => statement.setLong(parameterIndex, value)
      }
    }
  }

  implicit def BoxedLongToParameter(value: java.lang.Long): ParameterValue[java.lang.Long] = {
    BoxedLongParameter(value)
  }

  case class JavaMapParameter[K, V](value: java.util.Map[K, V]) extends ParameterValue[java.util.Map[K, V]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setMap[K, V](parameterIndex, value)
    }
  }

  implicit def JavaMapToParameter[K, V](value: java.util.Map[K, V]): ParameterValue[java.util.Map[K, V]] = {
    JavaMapParameter[K, V](value)
  }

  case class MapParameter[K, V](value: Map[K, V]) extends ParameterValue[Map[K, V]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setMap[K, V](parameterIndex, value)
    }
  }

  implicit def MapToParameter[K, V](value: Map[K, V]): ParameterValue[Map[K, V]] = {
    MapParameter[K, V](value)
  }


  case class OptionParameter[T](value: Option[T])(implicit innerParameterValue: ParameterValue[T]) extends ParameterValue[Option[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case None =>
          statement.setToNull(parameterIndex)
        case Some(innerValue) =>
          innerParameterValue.set(statement, parameterIndex)
      }
    }
  }

  implicit def OptionToParameter[T](value: Option[T])(implicit innerParameterValue: ParameterValue[T]): ParameterValue[Option[T]] = {
    OptionParameter[T](value)
  }

  case class SetParameter[T](value: Set[T]) extends ParameterValue[Set[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setSet[T](parameterIndex, value)
    }
  }

  implicit def SetToParameter[T](value: Set[T]): ParameterValue[Set[T]] = {
    SetParameter[T](value)
  }

  case class JavaSetParameter[T](value: java.util.Set[T]) extends ParameterValue[java.util.Set[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setSet[T](parameterIndex, value)
    }
  }

  implicit def SeqToParameter[T](value: java.util.Set[T]): ParameterValue[java.util.Set[T]] = {
    JavaSetParameter[T](value)
  }

  case class StringParameter(value: String) extends ParameterValue[String] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setString(parameterIndex, value)
    }
  }

  implicit def StringToParameter(value: String): ParameterValue[String] = {
    StringParameter(value)
  }

  case class UUIDParameter(value: UUID) extends ParameterValue[UUID] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setUUID(parameterIndex, value)
    }
  }

  implicit def UUIDToParameter(value: UUID): ParameterValue[UUID] = {
    UUIDParameter(value)
  }

  case class TokenParameter(value: Token) extends ParameterValue[Token] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setToken(parameterIndex, value)
    }
  }

  implicit def TokenToParameter(value: Token): ParameterValue[Token] = {
    TokenParameter(value)
  }

  case class TupleValueParameter(value: TupleValue) extends ParameterValue[TupleValue] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setTupleValue(parameterIndex, value)
    }
  }

  implicit def TupleValueToParameter(value: TupleValue): ParameterValue[TupleValue] = {
    TupleValueParameter(value)
  }

  case class UDTParameter(value: UDTValue) extends ParameterValue[UDTValue] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setUDTValue(parameterIndex, value)
    }
  }

  implicit def UDTValueToParameter(value: UDTValue): ParameterValue[UDTValue] = {
    UDTParameter(value)
  }

}
