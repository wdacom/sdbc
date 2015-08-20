package com.rocketfuel.sdbc.postgresql.jdbc.implementation

object IntervalConstants {

  val millisecondsPerSecond = 1000L

  //Constants from PostgreSQL's source: src/include/datatype/timestamp.h
  val secondsPerMinute = 60
  val secondsPerHour = 3600
  val daysPerYear = 365.25
  val secondsPerDay = 86400
  val daysPerMonth = 30

  //31557600
  val secondsPerYear = (daysPerYear * secondsPerDay).toInt

  //2592000
  val secondsPerMonth = daysPerMonth * secondsPerDay

}
