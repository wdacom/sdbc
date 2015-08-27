package com.rocketfuel.sdbc.base.jdbc

import java.io.{InputStream, Reader}
import java.nio.ByteBuffer
import java.sql.{Array => _, _}
import java.time.format.DateTimeFormatter
import java.time.{OffsetTime, OffsetDateTime}
import java.util
import java.util.UUID
import scodec.bits.ByteVector

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

object QLong extends ToParameter with QLongImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: Long => l
    case l: java.lang.Long => l
  }
}

trait QLongImplicits {
  implicit def LongToParameterValue(x: Long): ParameterValue[Long] = QLong(x)

  implicit def BoxedLongToParameterValue(x: java.lang.Long): ParameterValue[Long] = Long.unbox(x)
}

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

object QInt extends ToParameter with QIntImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case i: Int => i
    case i: java.lang.Integer => i
  }
}

trait QIntImplicits {
  implicit def IntToParameterValue(x: Int): ParameterValue[Int] = QInt(x)

  implicit def BoxedIntToParameterValue(x: java.lang.Integer): ParameterValue[Int] = Int.unbox(x)
}

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

object QShort extends ToParameter with QShortImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case s: Short => s
    case s: java.lang.Short => s
  }
}

trait QShortImplicits {
  implicit def ShortToParameterValue(x: Short): ParameterValue[Short] = QShort(x)

  implicit def BoxedShortToParameterValue(x: java.lang.Short): ParameterValue[Short] = Short.unbox(x)
}

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

object QByte extends ToParameter with QByteImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case b: Byte => b
    case b: java.lang.Byte => b
  }
}

trait QByteImplicits {
  implicit def ByteToParameterValue(x: Byte): ParameterValue[Byte] = QByte(x)

  implicit def BoxedByteToParameterValue(x: java.lang.Byte): ParameterValue[Byte] = Byte.unbox(x)
}

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

object QBytes extends ToParameter with QBytesImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case b: Array[Byte] => b
    case b: Array[java.lang.Byte] => b
    case b: ByteBuffer => b
    case b: ByteVector => b
  }
}

trait QBytesImplicits {
  implicit def ArrayByteToParameterValue(x: Array[Byte]): ParameterValue[Array[Byte]] = QBytes(x)

  implicit def ArrayBoxedByteToParameterValue(x: Array[java.lang.Byte]): ParameterValue[Array[Byte]] = QBytes(x.map(_.byteValue()))

  implicit def ByteBufferToParameterValue(x: ByteBuffer): ParameterValue[Array[Byte]] = QBytes(ByteVector(x).toArray)

  implicit def ByteVectorToParameterValue(x: ByteVector): ParameterValue[Array[Byte]] = QBytes(x.toArray)
}

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

object QFloat extends ToParameter with QFloatImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case f: Float => QFloat(f)
    case f: java.lang.Float => QFloat(f)
  }
}

trait QFloatImplicits {
  implicit def FloatToParameterValue(x: Float): ParameterValue[Float] = QFloat(x)

  implicit def BoxedFloatToParameterValue(x: java.lang.Float): ParameterValue[Float] = Float.unbox(x)
}

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

object QDouble extends ToParameter with QDoubleImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case d: Double => d
    case d: java.lang.Double => d
  }
}

trait QDoubleImplicits {
  implicit def DoubleToParameterValue(x: Double): ParameterValue[Double] = QDouble(x)

  implicit def BoxedDoubleToParameterValue(x: java.lang.Double): ParameterValue[Double] = Double.unbox(x)
}

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

object QBigDecimal extends ToParameter with QBigDecimalImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case d: BigDecimal => d
    case d: java.math.BigDecimal => d
  }
}

trait QBigDecimalImplicits {
  implicit def JavaBigDecimalToParameterValue(x: java.math.BigDecimal): ParameterValue[java.math.BigDecimal] = QBigDecimal(x)

  implicit def ScalaBigDecimalToParameterValue(x: scala.BigDecimal): ParameterValue[java.math.BigDecimal] = QBigDecimal(x.underlying)
}

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

object QTimestamp extends ToParameter with QTimestampImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case t: Timestamp => t
  }
}

trait QTimestampImplicits {
  implicit def TimestampToParameterValue(x: Timestamp): ParameterValue[Timestamp] = QTimestamp(x)
}

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

object QDate extends ToParameter with QDateImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case d: Date => d
    case d: java.util.Date => d
  }
}

trait QDateImplicits {
  implicit def DateToParameterValue(x: Date): ParameterValue[Date] = QDate(x)

  implicit def JavaDateToParameterValue(x: java.util.Date): ParameterValue[Date] = QDate(new Date(x.getTime))
}

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

object QTime extends ToParameter with QTimeImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case t: Time => t
  }
}

trait QTimeImplicits {
  implicit def TimeToParameterValue(x: Time): ParameterValue[Time] = QTime(x)
}

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

object QBoolean extends ToParameter with QBooleanImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case b: Boolean => b
    case b: java.lang.Boolean => b
  }

}

trait QBooleanImplicits {
  implicit def BooleanToParameterValue(x: Boolean): ParameterValue[Boolean] = QBoolean(x)

  implicit def BoxedBooleanToParameterValue(x: java.lang.Boolean): ParameterValue[Boolean] = Boolean.unbox(x)
}

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

object QString extends ToParameter with QStringImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case s: String => s
  }
}

trait QStringImplicits {
  implicit def StringToParameterValue(x: String): ParameterValue[String] = QString(x)
}

case class QReader(value: Reader) extends ParameterValue[Reader] {
  override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
    query.setCharacterStream(parameterIndex, value)
  }
}

object QReader extends ToParameter with QReaderImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case r: Reader => r
  }
}

trait QReaderImplicits {
  implicit def ReaderToParameterValue(x: Reader): ParameterValue[Reader] = QReader(x)
}

case class QInputStream(value: InputStream) extends ParameterValue[InputStream] {
  override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
    query.setBinaryStream(parameterIndex, value)
  }
}

object QInputStream extends ToParameter with QInputStreamImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case i: InputStream => i
  }
}

trait QInputStreamImplicits {
  implicit def InputStreamToParameterValue(x: InputStream): ParameterValue[InputStream] = QInputStream(x)
}

case class QUUID(value: UUID) extends ParameterValue[UUID] {

  override def set(
    preparedStatement: PreparedStatement,
    parameterIndex: Int
  ): Unit = {
    preparedStatement.setObject(parameterIndex, value)
  }
}

object QUUID extends ToParameter with QUUIDImplicits {
  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case u: UUID => u
  }

}
trait QUUIDImplicits {
  implicit def UUIDToParameterValue(x: UUID): ParameterValue[UUID] = QUUID(x)
}

case class QAnyRef(value: AnyRef) extends ParameterValue[AnyRef] {

  override def set(
    preparedStatement: PreparedStatement,
    parameterIndex: Int
  ): Unit = {
    preparedStatement.setObject(parameterIndex, value)
  }
}

object QAnyRef extends ToParameter with QAnyRefImplicits {

  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case a: AnyRef => a
  }
}

trait QAnyRefImplicits {
  implicit def AnyRefToParameterValue(x: AnyRef): ParameterValue[AnyRef] = QAnyRef(x)
}

object QInstant extends ToParameter with QInstantImplicits {

  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case i: java.time.Instant => i
  }

}

trait QInstantImplicits {
  implicit def InstantToParameterValue(x: java.time.Instant): ParameterValue[Timestamp] = {
    QTimestamp(Timestamp.from(x))
  }
}

object QLocalDate extends ToParameter with QLocalDateImplicits {

  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: java.time.LocalDate => l
  }

}

trait QLocalDateImplicits {
  implicit def LocalDateToParameterValue(x: java.time.LocalDate): ParameterValue[Date] = {
    QDate(Date.valueOf(x))
  }
}

object QLocalTime extends ToParameter with QLocalTimeImplicits {

  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: java.time.LocalTime => l
  }

}

trait QLocalTimeImplicits {
  implicit def LocalTimeToParameterValue(x: java.time.LocalTime): ParameterValue[Time] = {
    QTime(Time.valueOf(x))
  }
}

object QLocalDateTime extends ToParameter with QLocalDateTimeImplicits {

  override val toParameter: PartialFunction[Any, ParameterValue[_]] = {
    case l: java.time.LocalDateTime => l
  }

}

trait QLocalDateTimeImplicits {
  implicit def LocalDateTimeToParameterValue(x: java.time.LocalDateTime): ParameterValue[Timestamp] = {
    QTimestamp(Timestamp.valueOf(x))
  }
}

case class SdbcOffsetDateTimeFormatter(formatter: DateTimeFormatter)

case class QOffsetDateTime(value: OffsetDateTime)(implicit formatter: SdbcOffsetDateTimeFormatter) extends ParameterValue[OffsetDateTime] {

  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setString(parameterIndex, formatter.formatter.format(value))
  }

}

object QOffsetDateTime extends QOffsetDateTimeImplicits {

  def toParameter(implicit formatter: SdbcOffsetDateTimeFormatter): PartialFunction[Any, ParameterValue[_]] = {
    case o: OffsetDateTime => o
  }

}

trait QOffsetDateTimeImplicits {
  implicit def OffsetDateTimeToParameterValue(x: OffsetDateTime)(implicit formatter: SdbcOffsetDateTimeFormatter): ParameterValue[OffsetDateTime] = QOffsetDateTime(x)
}

case class SdbcOffsetTimeFormatter(formatter: DateTimeFormatter)

case class QOffsetTime(override val value: java.time.OffsetTime)(implicit formatter: SdbcOffsetTimeFormatter) extends ParameterValue[OffsetTime] {

  override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setString(parameterIndex, formatter.formatter.format(value))
  }

}

object QOffsetTime extends QOffsetTimeImplicits  {
  def toParameter(implicit formatter: SdbcOffsetTimeFormatter): PartialFunction[Any, ParameterValue[_]] = {
    case o: OffsetTime => o
  }
}

trait QOffsetTimeImplicits {
  implicit def OffsetTimeToParameterValue(x: OffsetTime)(implicit formatter: SdbcOffsetTimeFormatter): ParameterValue[OffsetTime] = QOffsetTime(x)
}
