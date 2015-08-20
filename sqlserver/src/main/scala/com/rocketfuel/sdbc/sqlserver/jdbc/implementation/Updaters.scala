package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.sqlserver.jdbc
import com.rocketfuel.sdbc.sqlserver.jdbc.HierarchyId

import scala.xml.Node

trait Updaters
  extends DefaultUpdaters
  with DateTimeFormatterUpdater {
  self: HasDateTimeFormatter =>

  override implicit val UUIDUpdater: Updater[UUID] = new Updater[UUID] {
    override def update(row: MutableRow, columnIndex: Int, x: UUID): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val HierarchyUpdater: Updater[HierarchyId] = new Updater[HierarchyId] {
    override def update(row: MutableRow, columnIndex: Int, x: jdbc.HierarchyId): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

  implicit val XmlUpdater: Updater[Node] = new Updater[Node] {
    override def update(row: MutableRow, columnIndex: Int, x: Node): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

}