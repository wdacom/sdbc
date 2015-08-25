package com.rocketfuel.sdbc.base.jdbc

import java.io.{InputStream, Reader}
import java.nio.ByteBuffer
import java.sql.{Array => _, _}
import java.time.{OffsetTime, OffsetDateTime}
import java.util
import java.util.UUID

import scodec.bits.ByteVector

trait LongParameter {

  case class QLong(value: Long) extends ParameterValue[Long] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setLong(
        parameterIndex,
        value
      )
    }
  }

  object QLong extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case l: Long => l
      case l: java.lang.Long => l
    }
  }

  implicit def LongToParameterValue(x: Long): ParameterValue[Long] = QLong(x)

  implicit def BigDecimalToParameterValue(x: java.lang.Long): ParameterValue[Long] = Long.unbox(x)

}

trait IntParameter {

  case class QInt(value: Int) extends ParameterValue[Int] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setInt(
        parameterIndex,
        value
      )
    }
  }

  object QInt extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case i: Int => i
      case i: java.lang.Integer => i
    }
  }

  implicit def IntToParameterValue(x: Int): ParameterValue[Int] = QInt(x)

  implicit def BoxedIntToParameterValue(x: java.lang.Integer): ParameterValue[Int] = Int.unbox(x)

}

trait ShortParameter {

  case class QShort(value: Short) extends ParameterValue[Short] {

    override def set(
      statement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setShort(
        parameterIndex,
        value
      )
    }
  }

  object QShort extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case s: Short => s
      case s: java.lang.Short => s
    }
  }

  implicit def ShortToParameterValue(x: Short): ParameterValue[Short] = QShort(x)
  
  implicit def BoxedShortToParameterValue(x: java.lang.Short): ParameterValue[Short] = Short.unbox(x)

}

trait ByteParameter {

  case class QByte(value: Byte) extends ParameterValue[Byte] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setByte(
        parameterIndex,
        value
      )
    }
  }

  object QByte extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case b: Byte => b
      case b: java.lang.Byte => b
    }
  }
  
  implicit def ByteToParameterValue(x: Byte): ParameterValue[Byte] = QByte(x)
  
  implicit def BoxedByteToParameterValue(x: java.lang.Byte): ParameterValue[Byte] = Byte.unbox(x)

}

trait BytesParameter {

  case class QBytes(value: Array[Byte]) extends ParameterValue[Array[Byte]] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBytes(
        parameterIndex,
        value
      )
    }

    override def hashCode(): Int = {
      util.Arrays.hashCode(value)
    }

    override def equals(obj: scala.Any): Boolean = {
      obj.isInstanceOf[QBytes] && value.sameElements(obj.asInstanceOf[QBytes].value)
    }
  }

  object QBytes extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case b: Array[Byte] => b
      case b: Array[java.lang.Byte] => b
      case b: ByteBuffer => b
      case b: ByteVector => b
    }
  }

  implicit def ArrayByteToParameterValue(x: Array[Byte]): ParameterValue[Array[Byte]] = QBytes(x)

  implicit def ArrayBoxedByteToParameterValue(x: Array[java.lang.Byte]): ParameterValue[Array[Byte]] = QBytes(x.map(_.byteValue()))

  implicit def ByteBufferToParameterValue(x: ByteBuffer): ParameterValue[Array[Byte]] = QBytes(ByteVector(x).toArray)

  implicit def ByteVectorToParameterValue(x: ByteVector): ParameterValue[Array[Byte]] = QBytes(x.toArray)

}

trait FloatParameter {

  case class QFloat(value: Float) extends ParameterValue[Float] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setFloat(
        parameterIndex,
        value
      )
    }
  }

  object QFloat extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case f: Float => f
      case f: java.lang.Float => f
    }
  }

  implicit def FloatToParameterValue(x: Float): ParameterValue[Float] = QFloat(x)

  implicit def BoxedFloatToParameterValue(x: java.lang.Float): ParameterValue[Float] = Float.unbox(x)

}

trait DoubleParameter {

  case class QDouble(value: Double) extends ParameterValue[Double] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setDouble(
        parameterIndex,
        value
      )
    }
  
  }

  object QDouble extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case d: Double => d
      case d: java.lang.Double => d
    }
  }

  implicit def DoubleToParameterValue(x: Double): ParameterValue[Double] = QDouble(x)
  
  implicit def BoxedDoubleToParameterValue(x: java.lang.Double): ParameterValue[Double] = Double.unbox(x)

}

trait BigDecimalParameter {

  case class QBigDecimal(value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBigDecimal(
        parameterIndex,
        value
      )
    }

  }

  object QBigDecimal extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case d: BigDecimal => d
      case d: java.math.BigDecimal => d
    }
  }

  implicit def JavaBigDecimalToParameterValue(x: java.math.BigDecimal): ParameterValue[java.math.BigDecimal] = QBigDecimal(x)

  implicit def ScalaBigDecimalToParameterValue(x: scala.BigDecimal): ParameterValue[java.math.BigDecimal] = QBigDecimal(x.underlying)

}

trait TimestampParameter {

  case class QTimestamp(value: Timestamp) extends ParameterValue[Timestamp] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setTimestamp(
        parameterIndex,
        value
      )
    }

  }

  object QTimestamp extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case t: Timestamp => t
    }
  }

  implicit def TimestampToParameterValue(x: Timestamp): ParameterValue[Timestamp] = QTimestamp(x)

}

trait DateParameter {

  case class QDate(value: Date) extends ParameterValue[Date] {
    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setDate(
        parameterIndex,
        value
      )
    }
  }

  object QDate extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case d: Date => d
      case d: java.util.Date => d
    }
  }

  implicit def DateToParameterValue(x: Date): ParameterValue[Date] = QDate(x)

  implicit def JavaDateToParameterValue(x: java.util.Date): ParameterValue[Date] = QDate(new Date(x.getTime))

}

trait TimeParameter {

  case class QTime(value: Time) extends ParameterValue[Time] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setTime(
        parameterIndex,
        value
      )
    }
  }

  object QTime extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case t: Time => t
    }
  }

  implicit def TimeToParameterValue(x: Time): ParameterValue[Time] = QTime(x)
  
}

trait BooleanParameter {

  case class QBoolean(value: Boolean) extends ParameterValue[Boolean] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBoolean(
        parameterIndex,
        value
      )
    }
  }

  object QBoolean extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case b: Boolean => b
      case b: java.lang.Boolean => b
    }
  }

  implicit def BooleanToParameterValue(x: Boolean): ParameterValue[Boolean] = QBoolean(x)

  implicit def BoxedBooleanToParameterValue(x: java.lang.Boolean): ParameterValue[Boolean] = Boolean.unbox(x)

}

trait StringParameter {

  case class QString(value: String) extends ParameterValue[String] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setString(
        parameterIndex,
        value
      )
    }
  }

  object QString extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case s: String => s
    }
  }

  implicit def StringToParameterValue(x: String): ParameterValue[String] = QString(x)
  
}

trait ReaderParameter {

  case class QReader(value: Reader) extends ParameterValue[Reader] {
    override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
      query.setCharacterStream(parameterIndex, value)
    }
  }

  object QReader extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case r: Reader => r
    }
  }

  implicit def ReaderToParameterValue(x: Reader): ParameterValue[Reader] = QReader(x)

}

trait InputStreamParameter {

  case class QInputStream(value: InputStream) extends ParameterValue[InputStream] {
    override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
      query.setBinaryStream(parameterIndex, value)
    }
  }

  object QInputStream extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case i: InputStream => i
    }
  }

  implicit def InputStreamToParameterValue(x: InputStream): ParameterValue[InputStream] = QInputStream(x)

}

trait UUIDParameter {

  case class QUUID(value: UUID) extends ParameterValue[UUID] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

  object QUUID extends ToParameter {
    override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
      case u: UUID => u
    }
  }

  implicit def UUIDToParameterValue(x: UUID): ParameterValue[UUID] = QUUID(x)
  
}

trait AnyRefParameter {

  case class QAnyRef(value: AnyRef) extends ParameterValue[AnyRef] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

  implicit def AnyRefToParameterValue(x: AnyRef): ParameterValue[AnyRef] = QAnyRef(x)
  
}

trait InstantParameter {
  self: TimestampParameter =>

  val toInstantParameter: PartialFunction[Any, ParameterValue[_]] = {
    case i: java.time.Instant => i
  }

  implicit def InstantToParameterValue(x: java.time.Instant): ParameterValue[Timestamp] = Timestamp.from(x)

}

trait LocalDateParameter {
  self: DateParameter =>

  val toLocalDateParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: java.time.LocalDate => l
  }

  implicit def LocalDateToParameterValue(x: java.time.LocalDate): ParameterValue[Date] = Date.valueOf(x)

}

trait LocalTimeParameter {
  self: TimeParameter =>

  val toLocalTimeParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: java.time.LocalTime => l
  }

  implicit def LocalTimeToParameterValue(x: java.time.LocalTime): ParameterValue[Time] = Time.valueOf(x)

}

trait LocalDateTimeParameter {
  self: TimestampParameter =>

  val toLocalDateTimeParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: java.time.LocalDateTime => l
  }

  implicit def LocalDateTimeToParameterValue(x: java.time.LocalDateTime): ParameterValue[Timestamp] = Timestamp.valueOf(x)

}

trait OffsetDateTimeParameter {
  self: HasOffsetDateTimeFormatter =>

  case class QOffsetDateTime(value: OffsetDateTime) extends ParameterValue[OffsetDateTime] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, offsetDateTimeFormatter.format(value))
    }

  }

  val toOffsetDateTimeParameter: PartialFunction[Any, ParameterValue[_]] = {
    case o: OffsetDateTime => o
  }

  implicit def OffsetDateTimeToParameterValue(x: OffsetDateTime): ParameterValue[OffsetDateTime] = QOffsetDateTime(x)
  
}

trait OffsetTimeParameter {
  self: HasOffsetTimeFormatter =>

  case class QOffsetTime(override val value: java.time.OffsetTime) extends ParameterValue[OffsetTime] {

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, offsetTimeFormatter.format(value))
    }

  }

  val toOffsetTimeParameter: PartialFunction[Any, ParameterValue[_]] = {
    case o: OffsetTime => o
  }

  implicit def OffsetTimeToParameterValue(x: OffsetTime): ParameterValue[OffsetTime] = QOffsetTime(x)
  
}
