package com.wda.sdbc.postgresql

import org.joda.time.Duration
import org.postgresql.util.PGInterval

trait DurationImplicits {
  implicit def DurationToPGInterval(value: Duration): PGInterval = {
    val millisRemainder = value.getMillis % IntervalConstants.millisecondsPerSecond
    val totalSeconds = value.getStandardSeconds
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

  implicit def PGIntervalToDuration(value: PGInterval): Duration = {
    val millis = (value.getSeconds - value.getSeconds.floor) * IntervalConstants.millisecondsPerSecond
    var seconds = 0L
    seconds += value.getSeconds.toLong
    seconds += value.getMinutes * IntervalConstants.secondsPerMinute
    seconds += value.getHours * IntervalConstants.secondsPerHour
    seconds += value.getDays * IntervalConstants.secondsPerDay
    seconds += value.getMonths * IntervalConstants.secondsPerMonth
    seconds += value.getYears * IntervalConstants.secondsPerYear
    new Duration(seconds * 1000 + millis.toLong)
  }
}
