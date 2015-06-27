package com.wda.sdbc
package sqlserver

import java.sql.{Timestamp, SQLException}
import java.util.UUID

import com.wda.sdbc.base._
import org.joda.time.{Instant, DateTime}

import scala.xml.{XML, Node}

trait Getters
  extends AnyRefGetter
  with BooleanGetter
  with ByteGetter
  with BytesGetter
  with DateGetter
  with DoubleGetter
  with FloatGetter
  with InputStreamGetter
  with IntGetter
  with JavaBigDecimalGetter
  with LongGetter
  with ReaderGetter
  with ScalaBigDecimalGetter
  with ShortGetter
  with StringGetter
  with TimeGetter
  with LocalDateTimeGetter
  with DateTimeGetter {
  self: Row with Getter with HierarchyId with HasDateTimeFormatter =>

  implicit val UUIDGetter: Getter[UUID] = new Getter[UUID] {
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

  implicit def NullableHierarchyIdSingleton(row: Row): Option[HierarchyId] = HierarchyIdGetter(row, 1)

  /**
   * jTDS is unable to parse datetimeoffsets as timestamps, so use our custom parser
   * if necessary.
   */
  implicit val TimestampGetter: Getter[Timestamp] = new Getter[Timestamp] {
    def apply(
      row: Row,
      columnIndex: Int
    ): Option[Timestamp] = {
      try {
        Option(row.getTimestamp(columnIndex))
      } catch {
        case e: SQLException =>
          DateTimeGetter(row, columnIndex).map(t => new Timestamp(t.getMillis))
      }
    }
  }

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

  implicit def NullableXMLSingleton(row: Row): Option[Node] = XMLGetter(row, 1)

  implicit def XMLSingleton(row: Row): Node = NullableXMLSingleton(row).get

  /**
   * The JTDS driver sometimes fails to parse timestamps, so when it fails, use our own parser.
   */
  implicit val InstantGetter: Getter[Instant] = new Getter[Instant] {
    def apply(row: Row, columnIndex: Int): Option[Instant] = {
      try {
        Option(row.getTimestamp(columnIndex)).map(t => new Instant(t.getTime))
      } catch {
        case e: SQLException =>
          val dt = DateTimeGetter(row, columnIndex)
          val i = dt.map(_.toInstant)
          i
      }
    }
  }

}
