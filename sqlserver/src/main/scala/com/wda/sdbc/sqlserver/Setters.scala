package com.wda.sdbc
package sqlserver

import java.util.UUID

import com.wda.sdbc.base._

import scala.xml.Elem

trait Setters extends Java8DefaultSetters {
  self: Row with ParameterValue with ParameterValues with HierarchyId =>

  implicit def HierarchyIdToParameterValue(x: HierarchyId): ParameterValue[HierarchyId] = QHierarchyId(x)
  implicit def UUIDToParameterValue(x: UUID): ParameterValue[UUID] = QUUID(x)
  implicit def XmlToParameterValue(x: Elem): ParameterValue[Elem] = QXML(x)

}
