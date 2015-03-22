package com.wda.sdbc.base

import java.sql.PreparedStatement
import java.time.OffsetTime
import java.time.format.DateTimeFormatter

trait HasJava8TimeFormatter {
  self: Row with Getter with ParameterValue =>

  val timeFormatter: DateTimeFormatter

  case class QOffsetTime(value: java.time.OffsetTime) extends ParameterValue[OffsetTime] {
    type T = java.time.OffsetTime

    override def asJDBCObject: AnyRef = timeFormatter.format(value)


    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setString(parameterIndex, timeFormatter.format(value))
    }

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateString(columnIndex, timeFormatter.format(value))
    }
  }

  implicit val OffsetTimeGetter = new Parser[OffsetTime] {
    override def parse(asString: String): OffsetTime = {
      val parsed = timeFormatter.parse(asString)
      OffsetTime.from(parsed)
    }
  }

  implicit def OffsetTimeToParameterValue(time: OffsetTime): ParameterValue[OffsetTime] = QOffsetTime(time)

}
