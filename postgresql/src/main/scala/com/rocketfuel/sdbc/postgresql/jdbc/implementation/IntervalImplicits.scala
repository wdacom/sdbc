package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.util.concurrent.TimeUnit

import org.joda.time.{Duration => JodaDuration}
import org.postgresql.util.PGInterval
import scala.concurrent.duration.{Duration => ScalaDuration}

trait IntervalImplicits {

  private def MillisecondsToPGInterval(millis: Long): PGInterval = {
    val millisRemainder = millis % IntervalConstants.millisecondsPerSecond
    val totalSeconds = millis / IntervalConstants.millisecondsPerSecond
    val years = totalSeconds / IntervalConstants.secondsPerYear
    val yearRemainder = totalSeconds % IntervalConstants.secondsPerYear
    val months = yearRemainder / IntervalConstants.secondsPerMonth
    val monthRemainder = yearRemainder % IntervalConstants.secondsPerMonth
    val days = monthRemainder / IntervalConstants.secondsPerDay
    val dayRemainder = monthRemainder % IntervalConstants.secondsPerDay
    val hours = dayRemainder / IntervalConstants.secondsPerHour
    val hoursRemainder = dayRemainder % IntervalConstants.secondsPerHour
    val minutes = hoursRemainder / IntervalConstants.secondsPerMinute
    val seconds = (hoursRemainder % IntervalConstants.secondsPerMinute).toDouble + (millisRemainder.toDouble / 1000)
    new PGInterval(
      years.toInt,
      months.toInt,
      days.toInt,
      hours.toInt,
      minutes.toInt,
      seconds
    )
  }

  private def PGIntervalToMilliseconds(value: PGInterval): Long = {
    val millis = (value.getSeconds - value.getSeconds.floor) * IntervalConstants.millisecondsPerSecond
    var seconds = 0L
    seconds += value.getSeconds.toLong
    seconds += value.getMinutes * IntervalConstants.secondsPerMinute
    seconds += value.getHours * IntervalConstants.secondsPerHour
    seconds += value.getDays * IntervalConstants.secondsPerDay
    seconds += value.getMonths * IntervalConstants.secondsPerMonth
    seconds += value.getYears * IntervalConstants.secondsPerYear
    seconds * 1000 + millis.toLong
  }

  implicit def JodaDurationToPGInterval(value: JodaDuration): PGInterval = {
    MillisecondsToPGInterval(value.getMillis)
  }

  implicit def PGIntervalToJodaDuration(value: PGInterval): JodaDuration = {
    new JodaDuration(PGIntervalToMilliseconds(value))
  }

  implicit def ScalaDurationToPGInterval(value: ScalaDuration): PGInterval = {
    MillisecondsToPGInterval(value.toMillis)
  }

  implicit def PGIntervalToScalaDuration(value: PGInterval): ScalaDuration = {
    ScalaDuration(PGIntervalToMilliseconds(value), TimeUnit.MILLISECONDS)
  }

}
