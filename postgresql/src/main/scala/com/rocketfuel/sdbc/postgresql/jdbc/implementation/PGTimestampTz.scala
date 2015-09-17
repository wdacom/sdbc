package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import com.rocketfuel.sdbc.base.ToParameter
import org.postgresql.util.PGobject

private[sdbc] class PGTimestampTz() extends PGobject() {

  setType("timestamptz")

  var DateTime: Option[DateTime] = None

  override def getValue: String = {
    DateTime.map(_.toString(dateTimeFormatter)).orNull
  }

  override def setValue(value: String): Unit = {
    this.DateTime = for {
      reallyValue <- Option(value)
    } yield {
        dateTimeFormatter.parseDateTime(reallyValue)
      }
  }

}

private[sdbc] object PGTimestampTz extends ToParameter {
  def apply(value: String): PGTimestampTz = {
    val tz = new PGTimestampTz()
    tz.setValue(value)
    tz
  }

  def apply(value: DateTime): PGTimestampTz = {
    val tz = new PGTimestampTz()
    tz.DateTime = Some(value)
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
