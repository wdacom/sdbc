package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId
import org.joda.time.{LocalTime, DateTime}

import scala.xml.Node

private[sdbc] trait Updaters
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
  with UpdateReader {

  override implicit val LocalTimeUpdater: Updater[LocalTime] = new Updater[LocalTime] {
    override def update(row: UpdatableRow, columnIndex: Int, x: LocalTime): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val DateTimeUpdater: Updater[DateTime] = new Updater[DateTime] {
    override def update(row: UpdatableRow, columnIndex: Int, x: DateTime): Unit = {
      row.updateString(columnIndex, x.toString(dateTimeFormatter))
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
