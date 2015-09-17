package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.DateTime
import org.joda.time.format._
import org.postgresql.util.PGobject

class TimeTz() extends PGobject() {

  setType("timetz")

  var time: Option[DateTime] = None

  override def getValue: String = {
    time.map(actualTime => actualTime.toString(ISODateTimeFormat.basicTime)).orNull
  }

  override def setValue(value: String): Unit = {
    time = Some(ISODateTimeFormat.basicDateTime.parseDateTime("1970-01-01T" + value))
  }

  override def equals(obj: scala.Any): Boolean = {
    obj.isInstanceOf[TimeTz] &&
    obj.asInstanceOf[TimeTz].time == time
  }

  override def hashCode(): Int = {
    time.hashCode()
  }
}

object TimeTz {
  def apply(dateTime: DateTime): TimeTz = {
    val truncated = dateTime.withYear(1970).withDayOfYear(1)
    val ttz = new TimeTz()
    ttz.time = Some(truncated)
    ttz
  }

  val builder = new DateTimeFormatterBuilder().
    appendHourOfDay(2).
    appendLiteral(":").
    appendMinuteOfHour(2).
    appendLiteral(":").
    appendSecondOfMinute(2).
    appendOptional(
      new DateTimeFormatterBuilder().
        appendLiteral('.').
        appendFractionOfSecond(1, 6).toParser
    )
    .appendTimeZoneOffset("+00", false, 1, 2)

  val formatter = builder.toFormatter

  val parser = builder.toParser
}

trait TimeTzImplicits {

}
