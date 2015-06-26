package com.wda.sdbc
package sqlserver

import java.sql.SQLException
import java.util.UUID

import com.wda.sdbc.base._
import org.joda.time.{Instant, DateTime}

import scala.xml.{XML, Node}

trait Getters extends DefaultGetters with DateTimeGetter {
  self: Row with Getter with HierarchyId with HasDateTimeFormatter =>

  override implicit val UUIDGetter: Getter[UUID] = new Getter[UUID] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[UUID] = {
      Option(row.getString(columnIndex)).map(UUID.fromString)
    }
  }

  implicit val HierarchyIdGetter = new Parser[HierarchyId] {
    override def parse(asString: String): HierarchyId = {
      HierarchyId(asString)
    }
  }

  implicit def NullableHierarchyIdSingleton(row: Row): Option[HierarchyId] = HierarchyIdGetter(
    row,
    1
  )

  implicit def HierarchyIdSingleton(row: Row): HierarchyId = NullableHierarchyIdSingleton(row).get

  implicit val XMLGetter: Getter[Node] = new Getter[Node] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Node] = {
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

  implicit def NullableXMLSingleton(row: Row): Option[Node] = XMLGetter(
    row,
    1
  )

  implicit def XMLSingleton(row: Row): Node = NullableXMLSingleton(row).get

  /**
   * The JTDS driver sometimes fails to parse timestamps, so when it fails, use our own parser.
   */
  override implicit val InstantGetter: Getter[Instant] = new Getter[Instant] {
    def apply(row: Row, columnIndex: Int): Option[Instant] = {
      try {
        Option(row.getTimestamp(columnIndex)).map(t => new Instant(t.getTime))
      } catch {
        case e: SQLException if e.getMessage.endsWith("cannot be converted to TIMESTAMP.") =>
          Option(row.getString(columnIndex)).map { asString =>
            val parsed = dateTimeFormatter.parseDateTime(asString)
            parsed.toInstant()
          }
      }
    }
  }
}
