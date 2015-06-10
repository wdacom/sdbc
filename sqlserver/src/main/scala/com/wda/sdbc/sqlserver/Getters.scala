package com.wda.sdbc
package sqlserver

import java.sql.SQLException
import java.time.{OffsetDateTime, Instant}
import java.util.UUID


import com.wda.sdbc.jdbc._

import scala.xml.{XML, Node}

trait Getters extends Java8DefaultGetters {
  self: HierarchyId with HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  override implicit val UUIDGetter: Getter[UUID] = new Parser[UUID] {
    override def parse(asString: String): UUID = {
      UUID.fromString(asString)
    }
  }

  implicit val HierarchyIdGetter = new Parser[HierarchyId] {
    override def parse(asString: String): HierarchyId = {
      HierarchyId(asString)
    }
  }

  implicit val XMLGetter: Getter[Node] = new Getter[Node] {

    override def apply(row: Row): Index => Option[Node] = { ix: Index =>
      for {
        clob <- Option(row.getClob(ix(row)))
      } yield {
        val stream = clob.getCharacterStream()
        try {
          XML.load(stream)
        } finally {
          util.Try(stream.close())
        }
      }
    }
  }

  /**
   * The JTDS driver fails to parse timestamps, so when it fails, use our own parser.
   */
  override implicit val InstantGetter = new Getter[Instant] {
    override def apply(row: Row): Index => Option[Instant] = { ix: Index =>
      try {
        Option(row.getTimestamp(ix(row))).map(_.toInstant)
      } catch {
        case e: SQLException if e.getMessage.endsWith("cannot be converted to TIMESTAMP.") =>
          for {
            asString <- Option(row.getString(ix(row)))
          } yield {
            val parsed = offsetDateTimeFormatter.parse(asString)
            OffsetDateTime.from(parsed).toInstant
          }
      }
    }
  }

}
