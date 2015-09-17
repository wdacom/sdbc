package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import org.joda.time.DateTime
import com.rocketfuel.sdbc.base.ToParameter
import org.postgresql.util.PGobject

class PGTimestampTz() extends PGobject() {

  setType("timestamptz")

  var dateTime: Option[DateTime] = None

  override def getValue: String = {
    dateTime.map(_.toString(datetimetzFormatter)).orNull
  }

  override def setValue(value: String): Unit = {
    this.dateTime = for {
      reallyValue <- Option(value)
    } yield {
        timestamptzFormatter.parseDateTime(reallyValue)
      }
  }

  override def toString: String = {
    getValue
  }
}

object PGTimestampTz extends ToParameter {
  def apply(value: String): PGTimestampTz = {
    val tz = new PGTimestampTz()
    tz.setValue(value)
    tz
  }

  def apply(value: DateTime): PGTimestampTz = {
    val tz = new PGTimestampTz()
    tz.dateTime = Some(value)
    tz
  }

  val toParameter: PartialFunction[Any, Any] = {
    case o: DateTime => PGTimestampTz(o)
  }
}

private[sdbc] trait PGTimestampTzImplicits {
  implicit def DateTimeToPGobject(o: DateTime): PGobject = {
    PGTimestampTz(o)
  }
}
