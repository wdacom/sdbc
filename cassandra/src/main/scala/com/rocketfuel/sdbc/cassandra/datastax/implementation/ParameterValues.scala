package com.rocketfuel.sdbc.cassandra.datastax.implementation

import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.{Date, UUID}
import com.datastax.driver.core.{BoundStatement, Token, TupleValue, UDTValue}
import scala.collection.convert.wrapAsJava._

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

  implicit def BoxedBooleanToParameter(value: java.lang.Boolean): ParameterValue[Option[Boolean]] = {
    OptionToParameter[Boolean](Option(value).map(_.booleanValue()))
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

  case class ArrayByteParameter(value: Array[Byte]) extends ParameterValue[Array[Byte]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setBytes(parameterIndex, ByteBuffer.wrap(value))
    }
  }

  implicit def IterableByteToParameter(value: Iterable[Byte]): ParameterValue[Array[Byte]] = {
    ArrayByteParameter(value.toArray)
  }

  implicit def ArrayByteToParameter(value: Array[Byte]): ParameterValue[Array[Byte]] = {
    ArrayByteParameter(value)
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

  case class BigDecimalParameter(value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDecimal(parameterIndex, value)
    }
  }

  implicit def BigDecimalToParameter(value: BigDecimal): ParameterValue[java.math.BigDecimal] = {
    BigDecimalParameter(value.underlying())
  }

  implicit def JavaBigDecimalToParameter(value: java.math.BigDecimal): ParameterValue[java.math.BigDecimal] = {
    BigDecimalParameter(value)
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

  implicit def BoxedDoubleToParameter(value: java.lang.Double): ParameterValue[Option[Double]] = {
    OptionToParameter[Double](Option(value).map(_.doubleValue()))
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

  implicit def BoxedFloatToParameter(value: java.lang.Float): ParameterValue[Option[Float]] = {
    OptionToParameter[Float](Option(value).map(_.floatValue()))
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

  implicit def BoxedIntToParameter(value: java.lang.Integer): ParameterValue[Option[Int]] = {
    OptionToParameter[Int](Option(value).map(_.intValue()))
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

  implicit def JavaListToParameter[T](value: java.util.List[T]): ParameterValue[java.util.List[T]] = {
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

  implicit def BoxedLongToParameter(value: java.lang.Long): ParameterValue[Option[Long]] = {
    OptionToParameter[Long](Option(value).map(_.longValue()))
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

  case class OptionParameter[T](value: Option[T])(implicit innerParameterValue: T => ParameterValue[T]) extends ParameterValue[Option[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      value match {
        case None =>
          statement.setToNull(parameterIndex)
        case Some(innerValue) =>
          innerParameterValue(innerValue).set(statement, parameterIndex)
      }
    }
  }

  implicit def OptionToParameter[T](value: Option[T])(implicit innerParameterValue: T => ParameterValue[T]): ParameterValue[Option[T]] = {
    OptionParameter[T](value)
  }

  case class SetParameter[T](value: java.util.Set[T]) extends ParameterValue[java.util.Set[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setSet[T](parameterIndex, value)
    }
  }

  implicit def SetToParameter[T](value: Set[T]): ParameterValue[java.util.Set[T]] = {
    SetParameter[T](value)
  }

  implicit def JavaSetToParameter[T](value: java.util.Set[T]): ParameterValue[java.util.Set[T]] = {
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

  case class BigIntegerParameter(value: BigInteger) extends ParameterValue[BigInteger] {
    override def set(statement: BoundStatement, parameterIndex: Int): Unit = {
      statement.setVarint(parameterIndex, value)
    }
  }

  implicit def BigIntegerToParameter(value: BigInteger): ParameterValue[BigInteger] = {
    BigIntegerParameter(value)
  }

}
