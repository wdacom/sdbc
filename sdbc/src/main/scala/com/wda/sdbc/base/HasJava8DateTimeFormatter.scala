package com.wda.sdbc.base

import java.sql.PreparedStatement
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

trait HasJava8DateTimeFormatter {
  self: Row with Getter with ParameterValue =>

  val dateTimeFormatter: DateTimeFormatter

  case class QOffsetDateTime(value: OffsetDateTime) extends ParameterValue[OffsetDateTime] {
    type T = java.time.OffsetDateTime

    override def asJDBCObject: AnyRef = dateTimeFormatter.format(value)


    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, dateTimeFormatter.format(value))
    }

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateString(columnIndex, dateTimeFormatter.format(value))
    }
  }

  implicit val OffsetDateTimeGetter = new Parser[OffsetDateTime] {
    override def parse(asString: String): OffsetDateTime = {
      val parsed = dateTimeFormatter.parse(asString)
      OffsetDateTime.from(parsed)
    }
  }

  implicit def OffsetDateTimeToParameterValue(time: OffsetDateTime): ParameterValue[OffsetDateTime] = QOffsetDateTime(time)

}
