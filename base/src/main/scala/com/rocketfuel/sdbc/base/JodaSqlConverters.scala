package com.rocketfuel.sdbc.base

import java.sql.{Time, Date, Timestamp}

import org.joda.time._

object JodaSqlConverters {

  implicit def InstantToTimestamp(value: Instant): Timestamp = {
    new Timestamp(value.getMillis)
  }

  implicit def TimestampToInstant(value: Timestamp): Instant = {
    new Instant(value.getTime)
  }

  implicit def LocalDateTimeToTimestamp(value: LocalDateTime): Timestamp = {
    new Timestamp(value.toDateTime().getMillis)
  }

  implicit def TimestampToLocalDateTime(value: Timestamp): LocalDateTime = {
    new LocalDateTime(value.getTime)
  }

  implicit def DateTimeToTimestamp(value: DateTime): Timestamp = {
    new Timestamp(value.getMillis)
  }

  implicit def TimestampToDateTime(value: Timestamp): DateTime = {
    new DateTime(value.getTime)
  }

  implicit def LocalDateToDate(value: LocalDate): Date = {
    new Date(value.toDateTimeAtStartOfDay.getMillis)
  }

  implicit def DateToLocalDate(value: Date): LocalDate = {
    new LocalDate(value.getTime)
  }

  implicit def LocalTimeToTime(value: LocalTime): Time = {
    new Time(value.toDateTimeToday.getMillis)
  }

  implicit def TimeToLocalTime(value: Time): LocalTime = {
    LocalTime.fromDateFields(value)
  }

}
