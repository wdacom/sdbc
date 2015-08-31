package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.sql.SQLException
import java.time.OffsetTime

import org.postgresql.util.PGobject

class PGTimeTz() extends PGobject() {

  var offsetTime: Option[OffsetTime] = None

  override def getValue: String = {
    offsetTime.map(offsetTimeFormatter.format).orNull
  }

  override def setValue(value: String): Unit = {
    this.offsetTime = for {
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
    tz.offsetTime = Some(value)
    tz
  }
}

trait PGTimeTzImplicits {
  implicit def OffsetTimeToPGobject(o: OffsetTime): PGobject = {
    PGTimeTz(o)
  }
  
  implicit def PGobjectToOffsetTime(x: PGobject): OffsetTime = {
    x match {
      case p: PGTimeTz =>
        p.offsetTime.get
      case _ =>
        throw new SQLException("column does not contain a ")
    }
  }
}
