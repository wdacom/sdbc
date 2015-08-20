package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.sql.SQLException
import java.util.UUID
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId
import org.joda.time.Instant

import scala.xml.{Node, XML}

trait Getters
  extends DefaultGetters
  with DateTimeFormatterGetter {
  self: HasDateTimeFormatter =>

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

    override def apply(row: Row, ix: Index): Option[Node] = {
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
   * The JTDS driver sometimes fails to parse timestamps, so when it fails, use our own parser.
   */
  override implicit val InstantGetter = new Getter[Instant] {
    override def apply(row: Row, ix: Index): Option[Instant] = {
      try {
        Option(row.getTimestamp(ix(row))).map(t => new Instant(t.getTime))
      } catch {
        case e: SQLException if e.getMessage.endsWith("cannot be converted to TIMESTAMP.") =>
          for {
            asString <- Option(row.getString(ix(row)))
          } yield {
            val parsed = dateTimeFormatter.parseDateTime(asString)
            parsed.toInstant
          }
      }
    }
  }

}
