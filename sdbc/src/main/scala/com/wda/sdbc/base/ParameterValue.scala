package com.wda.sdbc.base

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.util.UUID

import org.joda.time.{DateTime, LocalDateTime, Instant}

import scala.collection.immutable.Seq
import scala.reflect.runtime.universe._

trait ParameterValue {
  self: Row =>

  abstract class ParameterValue[T] {

    val value: T

    def asJDBCObject: AnyRef

    def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit

    def update(
      row: Row,
      columnIndex: Int
    ): Unit

    def update(
      row: Row,
      columnName: String
    ): Unit = {
      update(
        row,
        row.columnIndexes(columnName)
      )
    }
  }

  implicit def ToOptionParameterValue[T](v: T)(implicit conversion: T => ParameterValue[_]): Option[ParameterValue[_]] = {
    Some(conversion(v))
  }

  implicit def OptionToOptionParameterValue[T](v: Option[T])(implicit conversion: T => ParameterValue[_]): Option[ParameterValue[_]] = {
    v.map(conversion)
  }

}

trait LongParameter {
  self: ParameterValue with Row =>

  implicit class QLong(override val value: Long) extends ParameterValue[Long] {
    override def asJDBCObject: AnyRef = Long.box(value)

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setLong(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateLong(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedLongToParameterValue(x: java.lang.Long): ParameterValue[Long] = Long.unbox(x)

}

trait IntParameter {
  self: ParameterValue with Row =>

  implicit class QInt(override val value: Int) extends ParameterValue[Int] {
    override def asJDBCObject: AnyRef = Int.box(value)

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setInt(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateInt(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedIntToParameterValue(x: java.lang.Integer): ParameterValue[Int] = Int.unbox(x)

}

trait ShortParameter {
  self: ParameterValue with Row =>

  implicit class QShort(override val value: Short) extends ParameterValue[Short] {
    override def asJDBCObject: AnyRef = Short.box(value)

    override def set(
      statement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      statement.setShort(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateShort(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedShortToParameterValue(x: java.lang.Short): ParameterValue[Short] = Short.unbox(x)

}

trait ByteParameter {
  self: ParameterValue with Row =>

  implicit class QByte(override val value: Byte) extends ParameterValue[Byte] {
    override def asJDBCObject: AnyRef = Byte.box(value)

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setByte(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateByte(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedByteToParameterValue(x: java.lang.Byte): ParameterValue[Byte] = Byte.unbox(x)

}

trait BytesParameter {
  self: ParameterValue with Row =>

  implicit class QBytes(override val value: Array[Byte]) extends ParameterValue[Array[Byte]] {
    override def asJDBCObject: AnyRef = value

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBytes(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateBytes(
        columnIndex,
        value
      )
    }
  }

}

trait FloatParameter {
  self: ParameterValue with Row =>

  implicit class QFloat(override val value: Float) extends ParameterValue[Float] {
    override def asJDBCObject: AnyRef = Float.box(value)

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setFloat(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateFloat(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedFloatToParameterValue(x: java.lang.Float): ParameterValue[Float] = Float.unbox(x)

}

trait DoubleParameter {
  self: ParameterValue with Row =>

  implicit class QDouble(override val value: Double) extends ParameterValue[Double] {
    override def asJDBCObject: AnyRef = Double.box(value)

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setDouble(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateDouble(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedDoubleToParameterValue(x: java.lang.Double): ParameterValue[Double] = Double.unbox(x)

}

trait DecimalParameter {
  self: ParameterValue with Row =>

  implicit class QDecimal(override val value: java.math.BigDecimal) extends ParameterValue[java.math.BigDecimal] {
    override def asJDBCObject: AnyRef = value

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBigDecimal(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateBigDecimal(
        columnIndex,
        value
      )
    }
  }

  implicit def DecimalToParameterValue(x: scala.BigDecimal): ParameterValue[java.math.BigDecimal] = x.underlying

}

trait TimestampParameter {
  self: ParameterValue with Row =>

  implicit class QTimestamp(override val value: Timestamp) extends ParameterValue[Timestamp] {
    override def asJDBCObject: AnyRef = value

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setTimestamp(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateTimestamp(
        columnIndex,
        value
      )
    }
  }

}

trait DateParameter {
  self: ParameterValue with Row =>

  implicit class QDate(override val value: Date) extends ParameterValue[Date] {
    override def asJDBCObject: AnyRef = value

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setDate(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateDate(
        columnIndex,
        value
      )
    }
  }

}

trait TimeParameter {
  self: ParameterValue with Row =>

  implicit class QTime(override val value: Time) extends ParameterValue[Time] {
    override def asJDBCObject: AnyRef = value

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setTime(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateTime(
        columnIndex,
        value
      )
    }
  }

}

trait BooleanParameter {
  self: ParameterValue with Row =>

  implicit class QBoolean(override val value: Boolean) extends ParameterValue[Boolean] {
    override def asJDBCObject: AnyRef = Boolean.box(value)

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBoolean(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateBoolean(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedBooleanToParameterValue(x: java.lang.Boolean): ParameterValue[Boolean] = Boolean.unbox(x)

}

trait StringParameter {
  self: ParameterValue with Row =>

  implicit class QString(override val value: String) extends ParameterValue[String] {
    override def asJDBCObject: AnyRef = value

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setString(
        parameterIndex,
        value
      )
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateString(
        columnIndex,
        value
      )
    }
  }
}

trait ReaderParameter {
  self: ParameterValue with Row =>

  implicit class QReader(override val value: Reader) extends ParameterValue[Reader] {
    override def asJDBCObject: AnyRef = value

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateCharacterStream(columnIndex, value)
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setCharacterStream(parameterIndex, value)
    }
  }
}

trait InputStreamParameter {
  self: ParameterValue with Row =>

  implicit class QInputStreamReader(override val value: InputStream) extends ParameterValue[InputStream] {
    override def asJDBCObject: AnyRef = value

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateBinaryStream(columnIndex, value)
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setBinaryStream(parameterIndex, value)
    }
  }
}

trait UUIDParameter {
  self: ParameterValue with Row =>

  implicit class QUUID(override val value: UUID) extends ParameterValue[AnyRef] {
    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, value)
    }
  }
}

trait AnyRefParameter {
  self: ParameterValue with Row =>
  implicit class QAnyRef(override val value: AnyRef) extends ParameterValue[AnyRef] {
    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJDBCObject)
    }

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, value)
    }
  }

}

trait InstantParameter {
  self: ParameterValue with Row =>

  implicit class QInstant(override val value: Instant) extends ParameterValue[Instant] {
    val asTimestamp = new Timestamp(value.getMillis)

    override def asJDBCObject: AnyRef = asTimestamp

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateTimestamp(columnIndex, asTimestamp)
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setTimestamp(parameterIndex, asTimestamp)
    }
  }

}

trait LocalDateTimeParameter {
  self: ParameterValue with Row =>

  implicit class QLocalDateTime(override val value: LocalDateTime) extends ParameterValue[LocalDateTime] {
    val asDate = new java.sql.Date(value.toDateTime.getMillis)

    override def asJDBCObject: AnyRef = asDate

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateDate(columnIndex, asDate)
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setDate(parameterIndex, asDate)
    }
  }

}

trait DateTimeParameter {
  self: ParameterValue with Row with HasDateTimeFormatter =>

  implicit class QDateTime(override val value: DateTime) extends ParameterValue[DateTime] {
    override def asJDBCObject: AnyRef = value.toString(dateTimeFormatter)

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateString(columnIndex, value.toString(dateTimeFormatter))
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setString(parameterIndex, value.toString(dateTimeFormatter))
    }
  }

}
