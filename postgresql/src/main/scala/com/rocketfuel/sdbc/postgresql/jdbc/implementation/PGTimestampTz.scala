package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.time.OffsetDateTime

import org.postgresql.util.PGobject

class PGTimestampTz() extends PGobject() {

  private var value: Option[OffsetDateTime] = None

  override def getValue: String = {
    value.map(offsetDateTimeFormatter.format).orNull
  }

  override def setValue(value: String): Unit = {
    this.value = for {
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
    tz.value = Some(value)
    tz
  }
}
