package com.wda.sdbc.base

import org.joda.time._

trait JodaGetters {
  self: Getter with Row =>

  implicit val InstantGetter = new Getter[Instant] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Instant] = {
      Option(row.getString(columnIndex)).map(Instant.parse)
    }
  }

  implicit val DateTimeGetter = new Getter[DateTime] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[DateTime] = {
      Option(row.getString(columnIndex)).map(DateTime.parse)
    }
  }

  implicit val LocalDateTimeGetter = new Getter[LocalDateTime] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[LocalDateTime] = {
      Option(row.getString(columnIndex)).map(LocalDateTime.parse)
    }
  }

}
