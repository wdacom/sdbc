package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.time.OffsetTime

import org.postgresql.util.PGobject

class PGTimeTz() extends PGobject() {

  private var value: Option[OffsetTime] = None

  override def getValue: String = {
    value.map(offsetTimeFormatter.format).orNull
  }

  override def setValue(value: String): Unit = {
    this.value = for {
      reallyValue <- Option(value)
    } yield {
        val parsed = offsetTimeFormatter.parse(reallyValue)
        OffsetTime.from(parsed)
      }
  }

}

object PGTimeTz {
  def apply(value: String): PGTimeTz = {
    val tz = new PGTimeTz()
    tz.setValue(value)
    tz
  }

  def apply(value: OffsetTime): PGTimeTz = {
    val tz = new PGTimeTz()
    tz.value = Some(value)
    tz
  }
}
