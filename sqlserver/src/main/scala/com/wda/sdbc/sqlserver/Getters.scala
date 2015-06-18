package com.wda.sdbc
package sqlserver

import java.util.UUID

import com.wda.sdbc.base._

import scala.xml.{XML, Node}

trait Getters extends DefaultGetters {
  self: Row with Getter with HierarchyId =>

  override implicit val UUIDGetter: Getter[UUID] = new Getter[UUID] {
    override def apply(row: Row, columnIndex: Int): Option[UUID] = {
      Option(row.getString(columnIndex)).map(UUID.fromString)
    }
  }

  implicit val HierarchyIdGetter = new Parser[HierarchyId] {
    override def parse(asString: String): HierarchyId = {
      HierarchyId(asString)
    }
  }

  implicit def NullableHierarchyIdSingleton(row: Row): Option[HierarchyId] = HierarchyIdGetter(row, 1)

  implicit def HierarchyIdSingleton(row: Row): HierarchyId = NullableHierarchyIdSingleton(row).get

  implicit val XMLGetter: Getter[Node] = new Getter[Node] {
    override def apply(row: Row, columnIndex: Int): Option[Node] = {
      for {
        clob <- Option(row.getClob(columnIndex))
      } yield {
        val stream = clob.getCharacterStream
        try {
          XML.load(stream)
        } finally {
          util.Try(stream.close())
        }
      }
    }
  }

  implicit def NullableXMLSingleton(row: Row): Option[Node] = XMLGetter(row, 1)

  implicit def XMLSingleton(row: Row): Node = NullableXMLSingleton(row).get

}
