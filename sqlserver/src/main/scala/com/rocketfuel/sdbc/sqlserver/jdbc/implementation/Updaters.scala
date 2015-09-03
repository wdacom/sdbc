package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.time.OffsetDateTime
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId

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
  with LocalTimeUpdater {

  implicit val OffsetDateTimeUpdater: Updater[OffsetDateTime] = new Updater[OffsetDateTime] {
    override def update(row: UpdatableRow, columnIndex: Int, x: OffsetDateTime): Unit = {
      row.updateString(columnIndex, offsetDateTimeFormatter.format(x))
    }
  }

  implicit val UUIDUpdater: Updater[UUID] = new Updater[UUID] {
    override def update(row: UpdatableRow, columnIndex: Int, x: UUID): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val HierarchyUpdater: Updater[HierarchyId] = new Updater[HierarchyId] {
    override def update(row: UpdatableRow, columnIndex: Int, x: jdbc.HierarchyId): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val XmlUpdater: Updater[Node] = new Updater[Node] {
    override def update(row: UpdatableRow, columnIndex: Int, x: Node): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

}
