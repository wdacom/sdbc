package com.wda.sdbc.jdbc

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.util.UUID

trait LongParameter {

  implicit class QLong(override val value: Long) extends ParameterValue[Long] {

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

  implicit def BoxedLongToParameterValue(x: java.lang.Long): ParameterValue[Long] = Long.unbox(x)

}

trait IntParameter {

  implicit class QInt(override val value: Int) extends ParameterValue[Int] {

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

  implicit def BoxedIntToParameterValue(x: java.lang.Integer): ParameterValue[Int] = Int.unbox(x)

}

trait ShortParameter {

  implicit class QShort(override val value: Short) extends ParameterValue[Short] {

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

  implicit def BoxedShortToParameterValue(x: java.lang.Short): ParameterValue[Short] = Short.unbox(x)

}

trait ByteParameter {

  implicit class QByte(override val value: Byte) extends ParameterValue[Byte] {

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

  implicit def BoxedByteToParameterValue(x: java.lang.Byte): ParameterValue[Byte] = Byte.unbox(x)

}

trait BytesParameter {

  implicit class QBytes(override val value: Array[Byte]) extends ParameterValue[Array[Byte]] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBytes(
        parameterIndex,
        value
      )
    }
  }

}

trait FloatParameter {

  implicit class QFloat(override val value: Float) extends ParameterValue[Float] {

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

  implicit def BoxedFloatToParameterValue(x: java.lang.Float): ParameterValue[Float] = Float.unbox(x)

}

trait DoubleParameter {

  implicit class QDouble(override val value: Double) extends ParameterValue[Double] {

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

  implicit def BoxedDoubleToParameterValue(x: java.lang.Double): ParameterValue[Double] = Double.unbox(x)

}

trait DecimalParameter {

  implicit class QDecimal(override val value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {

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

  implicit def DecimalToParameterValue(x: scala.BigDecimal): ParameterValue[java.math.BigDecimal] = x.underlying

}

trait TimestampParameter {

  implicit class QTimestamp(override val value: Timestamp) extends ParameterValue[Timestamp] {

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

}

trait DateParameter {

  implicit class QDate(override val value: Date) extends ParameterValue[Date] {
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

}

trait TimeParameter {

  implicit class QTime(override val value: Time) extends ParameterValue[Time] {

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

}

trait BooleanParameter {

  implicit class QBoolean(override val value: Boolean) extends ParameterValue[Boolean] {

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

  implicit def BoxedBooleanToParameterValue(x: java.lang.Boolean): ParameterValue[Boolean] = Boolean.unbox(x)

}

trait StringParameter {

  implicit class QString(override val value: String) extends ParameterValue[String] {

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
}

trait ReaderParameter {

  implicit class QReader(override val value: Reader) extends ParameterValue[Reader] {
    override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
      query.setCharacterStream(parameterIndex, value)
    }
  }

}

trait InputStreamParameter {

  implicit class QInputStreamReader(override val value: InputStream) extends ParameterValue[InputStream] {
    override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
      query.setBinaryStream(parameterIndex, value)
    }
  }

}

trait UUIDParameter {

  implicit class QUUID(override val value: UUID) extends ParameterValue[AnyRef] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }
}

trait AnyRefParameter {

  implicit class QAnyRef(override val value: AnyRef) extends ParameterValue[AnyRef] {

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

}

trait InstantParameter {
  self: TimestampParameter =>

  implicit def InstantToParameterValue(x: java.time.Instant): ParameterValue[Timestamp] = Timestamp.from(x)

}

trait LocalDateParameter {
  self: DateParameter =>

  implicit def LocalDateToParameterValue(x: java.time.LocalDate): ParameterValue[Date] = Date.valueOf(x)

}

trait LocalTimeParameter {
  self: TimeParameter =>

  implicit def LocalTimeToParameterValue(x: java.time.LocalTime): ParameterValue[Time] = Time.valueOf(x)

}
trait LocalDateTimeParameter {
  self: TimestampParameter =>

  implicit def LocalDateTimeToParameterValue(x: java.time.LocalDateTime): ParameterValue[Timestamp] = Timestamp.valueOf(x)

}
