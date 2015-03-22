package com.wda.sdbc
package postgresql

import java.net.InetAddress
import java.time.Duration
import java.util.UUID

import com.wda.sdbc.base._
import org.json4s.{Formats, JValue}
import org.postgresql.util.PGInterval
import scala.xml.Elem

trait Setters extends Java8DefaultSetters {
  self: Row with ParameterValue with ParameterValues with IntervalImplicits =>

  implicit def DurationToParameterValue(duration: Duration): ParameterValue[PGInterval] = QPGInterval(duration)
  implicit def PGIntervalToParameterValue(interval: PGInterval): ParameterValue[PGInterval] = QPGInterval(interval)
  implicit def InetAddressToParameterValue(address: InetAddress): ParameterValue[InetAddress] = QInetAddress(address)
  implicit def JValueToParameterValue(j: JValue)(implicit formats: Formats): ParameterValue[JValue] = {
    QJSON(j)
  }
  implicit def LTreeToParameterValue(ltree: LTree): ParameterValue[LTree] = QLTree(ltree)
  implicit def UUIDToParameterValue(uuid: UUID): ParameterValue[UUID] = QUUID(uuid)
  implicit def XmlToParameterValue(x: Elem): ParameterValue[Elem] = QXML(x)

}
