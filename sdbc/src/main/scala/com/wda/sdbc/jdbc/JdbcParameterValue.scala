package com.wda.sdbc.jdbc

import java.io.{InputStream, Reader}
import java.sql.{Time, Date, Timestamp, PreparedStatement}
import java.util.UUID

import com.wda.sdbc.base

trait JdbcParameterValue {
  self: JdbcRow with base.ParameterValue =>

  abstract class JdbcParameterValue[T] extends ParameterValue[T] {

    val value: T

    def asJDBCObject: AnyRef

    def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
      ): Unit

    def update(
      row: MutableJdbcRow,
      columnIndex: Int
      ): Unit

    def update(
      row: MutableJdbcRow,
      columnName: String
      ): Unit = {
      update(
        row,
        row.columnIndexes(columnName)
      )
    }
  }

}

trait LongParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QLong(override val value: Long) extends JdbcParameterValue[Long] {
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
      row: MutableJdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateLong(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedLongToParameterValue(x: java.lang.Long): JdbcParameterValue[Long] = Long.unbox(x)

}

trait IntParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QInt(override val value: Int) extends JdbcParameterValue[Int] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateInt(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedIntToParameterValue(x: java.lang.Integer): JdbcParameterValue[Int] = Int.unbox(x)

}

trait ShortParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QShort(override val value: Short) extends JdbcParameterValue[Short] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateShort(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedShortToParameterValue(x: java.lang.Short): JdbcParameterValue[Short] = Short.unbox(x)

}

trait ByteParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QByte(override val value: Byte) extends JdbcParameterValue[Byte] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateByte(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedByteToParameterValue(x: java.lang.Byte): JdbcParameterValue[Byte] = Byte.unbox(x)

}

trait BytesParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QBytes(override val value: Array[Byte]) extends JdbcParameterValue[Array[Byte]] {
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
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QFloat(override val value: Float) extends JdbcParameterValue[Float] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateFloat(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedFloatToParameterValue(x: java.lang.Float): JdbcParameterValue[Float] = Float.unbox(x)

}

trait DoubleParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QDouble(override val value: Double) extends JdbcParameterValue[Double] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateDouble(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedDoubleToParameterValue(x: java.lang.Double): JdbcParameterValue[Double] = Double.unbox(x)

}

trait DecimalParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QDecimal(override val value: java.math.BigDecimal) extends JdbcParameterValue[java.math.BigDecimal] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateBigDecimal(
        columnIndex,
        value
      )
    }
  }

  implicit def DecimalToParameterValue(x: scala.BigDecimal): JdbcParameterValue[java.math.BigDecimal] = x.underlying

}

trait TimestampParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QTimestamp(override val value: Timestamp) extends JdbcParameterValue[Timestamp] {
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
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QDate(override val value: Date) extends JdbcParameterValue[Date] {
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
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QTime(override val value: Time) extends JdbcParameterValue[Time] {
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
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QBoolean(override val value: Boolean) extends JdbcParameterValue[Boolean] {
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
      row: JdbcRow,
      columnIndex: Int
      ): Unit = {
      row.updateBoolean(
        columnIndex,
        value
      )
    }
  }

  implicit def BoxedBooleanToParameterValue(x: java.lang.Boolean): JdbcParameterValue[Boolean] = Boolean.unbox(x)

}

trait StringParameter {
  self: JdbcParameterValue with JdbcRow =>

  implicit class QString(override val value: String) extends JdbcParameterValue[String] {
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
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QReader(override val value: Reader) extends JdbcParameterValue[Reader] {
    override def asJDBCObject: AnyRef = value

    override def update(
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QInputStreamReader(override val value: InputStream) extends JdbcParameterValue[InputStream] {
    override def asJDBCObject: AnyRef = value

    override def update(
      row: JdbcRow,
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
  self: JdbcParameterValue with JdbcRow =>

  implicit class QUUID(override val value: UUID) extends JdbcParameterValue[AnyRef] {
    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }

    override def update(row: JdbcRow, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, value)
    }
  }
}

trait AnyRefParameter {
  self: JdbcParameterValue with JdbcRow =>
  implicit class QAnyRef(override val value: AnyRef) extends JdbcParameterValue[AnyRef] {
    override def asJDBCObject: AnyRef = value

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJDBCObject)
    }

    override def update(row: JdbcRow, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, value)
    }
  }

}

trait InstantParameter {
  self: JdbcParameterValue with TimestampParameter =>

  implicit def InstantToParameterValue(x: java.time.Instant): JdbcParameterValue[Timestamp] = Timestamp.from(x)

}

trait LocalDateParameter {
  self: JdbcParameterValue with DateParameter =>

  implicit def LocalDateToParameterValue(x: java.time.LocalDate): JdbcParameterValue[Date] = Date.valueOf(x)

}

trait LocalTimeParameter {
  self: JdbcParameterValue with TimeParameter =>

  implicit def LocalTimeToParameterValue(x: java.time.LocalTime): JdbcParameterValue[Time] = Time.valueOf(x)

}
trait LocalDateTimeParameter {
  self: JdbcParameterValue with TimestampParameter =>

  implicit def LocalDateTimeToParameterValue(x: java.time.LocalDateTime): JdbcParameterValue[Timestamp] = Timestamp.valueOf(x)

}
