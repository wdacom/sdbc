package com.rocketfuel.sdbc.postgresql.jdbc

import org.joda.time.DateTime
import org.postgresql.util.PGobject

class TimeTz() extends PGobject() {

  setType("timetz")

  var time: Option[DateTime] = None

  override def getValue: String = {
    time.map(actualTime => actualTime.toString(implementation.timetzFormatter)).
      getOrElse(throw new IllegalStateException("setValue must be called first"))
  }

  override def setValue(value: String): Unit = {
    time = Some(implementation.timetzFormatter.parseDateTime("1970-01-01T" + value))
  }

  override def equals(obj: scala.Any): Boolean = {
    val compared = obj match {
      case objTime: TimeTz =>
        for {
          myTime <- time
          theirTime <- objTime.time
        } yield {
          theirTime.millisOfDay() == myTime.millisOfDay()
        }
      case _ =>
        None
    }

    compared.exists(identity)
  }

  override def hashCode(): Int = {
    time.hashCode()
  }

  override def toString: String = {
    getValue
  }
}

object TimeTz {
  def apply(dateTime: DateTime): TimeTz = {
    val truncated = dateTime.withYear(1970).withDayOfYear(1)
    val ttz = new TimeTz()
    ttz.time = Some(truncated)
    ttz
  }
}

trait TimeTzImplicits {

}
