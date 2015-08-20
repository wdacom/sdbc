package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc.LTree
import org.json4s.JValue
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval
import scala.xml.Node
import org.joda.time.{Duration => JodaDuration}
import scala.concurrent.duration.{Duration => ScalaDuration}

trait Updaters
  extends DefaultUpdaters
  with DateTimeFormatterUpdater {
  self: PgDateTimeFormatter with IntervalImplicits =>

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

  implicit val JodaDurationUpdater = new Updater[JodaDuration] {
    override def update(
      row: MutableRow,
      columnIndex: Int,
      x: JodaDuration
    ): Unit = {
      PGIntervalUpdater.update(row, columnIndex, JodaDurationToPGInterval(x))
    }
  }

  implicit val ScalaDurationUpdater = new Updater[ScalaDuration] {
    override def update(
      row: MutableRow,
      columnIndex: Int,
      x: ScalaDuration
    ): Unit = {
      PGIntervalUpdater.update(row, columnIndex, ScalaDurationToPGInterval(x))
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
