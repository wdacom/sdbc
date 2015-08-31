package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.sql.SQLException
import java.time.OffsetDateTime

import org.postgresql.util.PGobject

class PGTimestampTz() extends PGobject() {

  var offsetDateTime: Option[OffsetDateTime] = None
  
  override def getValue: String = {
    offsetDateTime.map(offsetDateTimeFormatter.format).orNull
  }

  override def setValue(value: String): Unit = {
    this.offsetDateTime = for {
      reallyValue <- Option(value)
    } yield {
        val parsed = offsetDateTimeFormatter.parse(reallyValue)
        OffsetDateTime.from(parsed)
      }
  }

}

object PGTimestampTz {
  def apply(value: String): PGTimestampTz = {
    val tz = new PGTimestampTz()
    tz.setValue(value)
    tz
  }

  def apply(value: OffsetDateTime): PGTimestampTz = {
    val tz = new PGTimestampTz()
    tz.offsetDateTime = Some(value)
    tz
  }
}

trait PGTimestampTzImplicits {
  implicit def OffsetDateTimeToPGobject(o: OffsetDateTime): PGobject = {
    PGTimestampTz(o)
  }
  
  implicit def PGobjectToOffsetDateTime(x: PGobject): OffsetDateTime = {
    x match {
      case p: PGTimestampTz =>
        p.offsetDateTime.get
      case _ =>
        throw new SQLException("column does not contain a timestamptz")
    }
  }
}
