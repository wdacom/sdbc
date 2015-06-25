package com.wda.sdbc.base

import java.sql.{Timestamp, PreparedStatement}

import org.joda.time.{DateTime, Instant}

trait JodaInstant {
  self: ParameterValue with Row =>

  implicit class QInstant(override val value: Instant) extends ParameterValue[Instant] {
    override def asJDBCObject: AnyRef = new Timestamp(value.getMillis)

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row.updateTimestamp(columnIndex, new Timestamp(value.getMillis))
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setTimestamp(parameterIndex, new Timestamp(value.getMillis))
    }
  }

}

trait JodaDateTime {
  self: ParameterValue with Row with JodaDateTimeFormatter =>

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
