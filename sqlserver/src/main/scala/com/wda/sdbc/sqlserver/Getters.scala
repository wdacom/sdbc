package com.wda.sdbc
package sqlserver

import java.sql.SQLException
import java.time.{OffsetDateTime, Instant}
import java.util.UUID

import com.wda.sdbc.base._

import scala.xml.{XML, Node}

trait Getters extends Java8DefaultGetters {
  self: Row with Getter with HierarchyId with HasJava8DateTimeFormatter =>

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

  /**
   * The JTDS driver fails to parse timestamps, so when it fails, use our own parser.
   */
  override implicit val InstantGetter = new Getter[Instant] {
    override def apply(row: Row, columnIndex: Int): Option[Instant] = {
      try {
        Option(row.underlying.getTimestamp(columnIndex)).map(_.toInstant())
      } catch {
        case e: SQLException if e.getMessage.endsWith("cannot be converted to TIMESTAMP.") =>
          Option(row.underlying.getString(columnIndex)).map { asString =>
            val parsed = dateTimeFormatter.parse(asString)
            OffsetDateTime.from(parsed).toInstant()
          }
      }
    }
  }

}
