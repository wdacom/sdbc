package com.wda.sdbc.cassandra

import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.Date
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

  case class ByteBufferParameter(value: ByteBuffer) extends ParameterValue[ByteBuffer] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setBytes(parameterIndex, value)
    }
  }

  case class IterableByteParameter(value: Iterable[Byte]) extends ParameterValue[Iterable[Byte]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setBytes(parameterIndex, ByteBuffer.wrap(value.toArray))
    }
  }

  case class DateParameter(value: Date) extends ParameterValue[Date] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDate(parameterIndex, value)
    }
  }

  case class DecimalParameter(value: BigDecimal) extends ParameterValue[BigDecimal] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDecimal(parameterIndex, value.underlying())
    }
  }

  case class JavaDecimalParameter(value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDecimal(parameterIndex, value)
    }
  }

  case class DoubleParameter(value: Double) extends ParameterValue[Double] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setDouble(parameterIndex, value)
    }
  }

  case class FloatParameter(value: Float) extends ParameterValue[Float] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setFloat(parameterIndex, value)
    }
  }

  case class InetParameter(value: InetAddress) extends ParameterValue[InetAddress] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setInet(parameterIndex, value)
    }
  }

  case class IntParameter(value: Int) extends ParameterValue[Int] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement
      .setInt(
          parameterIndex,
          value
        )
    }
  }

  case class SeqParameter[T](value: Seq[T]) extends ParameterValue[Seq[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setList[T](parameterIndex, value)
    }
  }

  case class JavaListParameter[T](value: java.util.List[T]) extends ParameterValue[java.util.List[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setList[T](parameterIndex, value)
    }
  }

  case class LongParameter(value: Long) extends ParameterValue[Long] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setLong(parameterIndex, value)
    }
  }

  case class JavaMapParameter[K, V](value: java.util.Map[K, V]) extends ParameterValue[java.util.Map[K, V]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setMap[K, V](parameterIndex, value)
    }
  }

  case class MapParameter[K, V](value: Map[K, V]) extends ParameterValue[Map[K, V]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setMap[K, V](parameterIndex, value)
    }
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

  case class SetParameter[T](value: Set[T]) extends ParameterValue[Set[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setSet[T](parameterIndex, value)
    }
  }

  case class JavaSetParameter[T](value: java.util.Set[T]) extends ParameterValue[java.util.Set[T]] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setSet[T](parameterIndex, value)
    }
  }

  case class StringParameter(value: String) extends ParameterValue[String] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setString(parameterIndex, value)
    }
  }

  case class TokenParameter(value: Token) extends ParameterValue[Token] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setToken(parameterIndex, value)
    }
  }

  case class TupleValueParameter(value: TupleValue) extends ParameterValue[TupleValue] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setTupleValue(parameterIndex, value)
    }
  }

  case class UTDParameter(value: UDTValue) extends ParameterValue[UDTValue] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setUDTValue(parameterIndex, value)
    }
  }

}
