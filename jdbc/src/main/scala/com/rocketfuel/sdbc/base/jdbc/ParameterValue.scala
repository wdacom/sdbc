package com.rocketfuel.sdbc.base.jdbc

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.util.UUID
import org.joda.time._
import com.rocketfuel.sdbc.base.JodaSqlConverters._

trait OptionParameter {

  implicit def ParameterValueToOptionParameterValue[T](value: T)(implicit toParam: T => ParameterValue[_]): Option[ParameterValue[_]] = {
    Some(toParam(value))
  }

  implicit def OptionToOptionParameterValue[T](value: Option[T])(implicit toParam: T => ParameterValue[_]): Option[ParameterValue[_]] = {
    value.map(toParam)
  }

}

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
  }

  implicit def BytesToParameterValue(x: Array[Byte]): ParameterValue[Array[Byte]] = QBytes(x)
  
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

  implicit def DoubleToParameterValue(x: Double): ParameterValue[Double] = QDouble(x)
  
  implicit def BoxedDoubleToParameterValue(x: java.lang.Double): ParameterValue[Double] = Double.unbox(x)

}

trait DecimalParameter {

  case class QDecimal(value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {

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

  implicit def DecimalToParameterValue(x: java.math.BigDecimal): ParameterValue[java.math.BigDecimal] = QDecimal(x)
  
  implicit def DecimalToParameterValue(x: scala.BigDecimal): ParameterValue[java.math.BigDecimal] = x.underlying

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

  implicit def TimestampToParameterValue(x: Timestamp): ParameterValue[Timestamp] = QTimestamp(x)

  implicit def LocalDateTimeToParameterValue(x: DateTime): ParameterValue[Timestamp] = QTimestamp(x)

  implicit def InstantToParameterValue(x: Instant): ParameterValue[Timestamp] = QTimestamp(x)

}

trait DateTimeParameter {
  self: TimestampParameter =>

  implicit def DateTimeToParameterValue(x: DateTime): ParameterValue[Timestamp] = QTimestamp(x)

}

trait DateTimeFormatterParameter {
  self: StringParameter with TimestampParameter with HasDateTimeFormatter =>

  implicit def DateTimeToParameterValue(x: DateTime): ParameterValue[String] = {
    QString(x.toString(dateTimeFormatter))
  }

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

  implicit def DateToParameterValue(x: Date): ParameterValue[Date] = QDate(x)

  implicit def LocalDateToParameterValue(x: LocalDate): ParameterValue[Date] = QDate(x)
  
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

  implicit def TimeToParameterValue(x: Time): ParameterValue[Time] = QTime(x)

  implicit def LocalTimeToParameterValue(x: LocalTime): ParameterValue[Time] = QTime(x)

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

  implicit def StringToParameterValue(x: String): ParameterValue[String] = QString(x)
  
}

trait ReaderParameter {

  case class QReader(value: Reader) extends ParameterValue[Reader] {
    override def set(query: PreparedStatement, parameterIndex: Int): Unit = {
      query.setCharacterStream(parameterIndex, value)
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
