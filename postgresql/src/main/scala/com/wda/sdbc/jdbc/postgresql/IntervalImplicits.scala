package com.wda.sdbc.jdbc.postgresql

import java.time.Duration

import org.postgresql.util.PGInterval

trait IntervalImplicits {

  implicit def DurationToPGInterval(value: Duration): PGInterval = {
    val nano = value.getNano.toDouble / IntervalConstants.nanosecondsPerSecond.toDouble
    val totalSeconds = value.getSeconds
    val years = totalSeconds / IntervalConstants.secondsPerYear
    val yearRemainder = totalSeconds % IntervalConstants.secondsPerYear
    val months = yearRemainder / IntervalConstants.secondsPerMonth
    val monthRemainder = yearRemainder % IntervalConstants.secondsPerMonth
    val days = monthRemainder / IntervalConstants.secondsPerDay
    val dayRemainder = monthRemainder % IntervalConstants.secondsPerDay
    val hours = dayRemainder / IntervalConstants.secondsPerHour
    val hoursRemainder = dayRemainder % IntervalConstants.secondsPerHour
    val minutes = hoursRemainder / IntervalConstants.secondsPerMinute
    val seconds = (hoursRemainder % IntervalConstants.secondsPerMinute).toDouble + nano
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
    val nanos = (value.getSeconds - value.getSeconds.floor) * IntervalConstants.nanosecondsPerSecond
    var seconds = 0L
    seconds += value.getSeconds.toLong
    seconds += value.getMinutes * IntervalConstants.secondsPerMinute
    seconds += value.getHours * IntervalConstants.secondsPerHour
    seconds += value.getDays * IntervalConstants.secondsPerDay
    seconds += value.getMonths * IntervalConstants.secondsPerMonth
    seconds += value.getYears * IntervalConstants.secondsPerYear
    java.time.Duration.ofSeconds(seconds, nanos.toLong)
  }

}
