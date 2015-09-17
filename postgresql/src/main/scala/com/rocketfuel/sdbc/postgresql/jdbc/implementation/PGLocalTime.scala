package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import org.joda.time.LocalTime

import com.rocketfuel.sdbc.base.ToParameter
import org.postgresql.util.PGobject

private[sdbc] class PGLocalTime() extends PGobject() {

  setType("time")

  var localTime: Option[LocalTime] = None

  override def getValue: String = {
    localTime.map(_.toString(timeFormatter)).orNull
  }

  override def setValue(value: String): Unit = {
    this.localTime = for {
      reallyValue <- Option(value)
    } yield {
        timeFormatter.parseLocalTime(reallyValue)
      }
  }

  override def toString: String = {
    getValue
  }

  override def hashCode(): Int = localTime.hashCode()
}

private[sdbc] object PGLocalTime extends ToParameter {
  def apply(l: LocalTime): PGLocalTime = {
    val t = new PGLocalTime()
    t.localTime = Some(l)
    t
  }

  override val toParameter: PartialFunction[Any, Any] = {
    case l: LocalTime => PGLocalTime(l)
  }
}

private[sdbc] trait PGLocalTimeImplicits {
  implicit def LocalTimeToPGobject(l: LocalTime): PGobject = {
    PGLocalTime(l)
  }
}
