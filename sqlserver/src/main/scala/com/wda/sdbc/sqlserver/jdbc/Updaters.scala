package com.wda.sdbc.sqlserver.jdbc

import java.util.UUID

import com.wda.sdbc.base.jdbc._

import scala.xml.Node

trait Updaters
  extends AnyRefUpdater
  with LongUpdater
  with IntUpdater
  with ShortUpdater
  with ByteUpdater
  with BytesUpdater
  with DoubleUpdater
  with FloatUpdater
  with JavaBigDecimalUpdater
  with ScalaBigDecimalUpdater
  with TimestampUpdater
  with DateUpdater
  with TimeUpdater
  with BooleanUpdater
  with StringUpdater
  with InputStreamUpdater
  with UpdateReader
  with LocalDateTimeUpdater
  with InstantUpdater
  with LocalDateUpdater
  with LocalTimeUpdater
  with OffsetDateTimeGetter
  with OffsetTimeGetter {
  self: HierarchyId with HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  implicit val UUIDUpdater: Updater[UUID] = new Updater[UUID] {
    override def update(row: MutableRow, columnIndex: Int, x: UUID): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val HierarchyUpdater: Updater[HierarchyId] = new Updater[HierarchyId] {
    override def update(row: MutableRow, columnIndex: Int, x: HierarchyId): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val XmlUpdater: Updater[Node] = new Updater[Node] {
    override def update(row: MutableRow, columnIndex: Int, x: Node): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

}