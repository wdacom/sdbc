package com.wda.sdbc.postgresql

import java.net.InetAddress

import com.wda.sdbc.jdbc.{MutableRow, Updater, Java8DefaultUpdaters}
import org.json4s.JValue
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.Node

trait Updaters extends Java8DefaultUpdaters {
  self: HasOffsetDateTimeFormatter with HasOffsetTimeFormatter =>

  implicit val InetAddressUpdater = new Updater[InetAddress] {
    override def update(row: MutableRow, columnIndex: Int, x: InetAddress): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val PGIntervalUpdater = new Updater[PGInterval] {
    override def update(
      row: MutableRow,
      columnIndex: Int,
      x: PGInterval
    ): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val JsonUpdater = new Updater[JValue] {
    override def update(
      row: MutableRow,
      columnIndex: Int,
      x: JValue
    ): Unit = {
      row.updateString(columnIndex, JsonMethods.compact(JsonMethods.render(x)))
    }
  }

  implicit val LTreeUpdater = new Updater[LTree] {
    override def update(
      row: MutableRow,
      columnIndex: Int,
      x: LTree
    ): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val XmlUpdater = new Updater[Node] {
    override def update(
      row: MutableRow,
      columnIndex: Int,
      x: Node
    ): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

}
