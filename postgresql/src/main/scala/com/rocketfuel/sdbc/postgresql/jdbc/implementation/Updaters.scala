package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc.LTree
import org.json4s.JValue
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scala.xml.Node

//PostgreSQL doesn't support Byte, so we don't use the default updaters.
trait Updaters
  extends AnyRefUpdater
  with LongUpdater
  with IntUpdater
  with ShortUpdater
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
  with UUIDUpdater
  with InputStreamUpdater
  with UpdateReader
  with LocalDateTimeUpdater
  with InstantUpdater
  with LocalDateUpdater
  with LocalTimeUpdater
  with OffsetDateTimeUpdater
  with OffsetTimeUpdater {
  self: DateTimeFormatters with HasOffsetTimeFormatter =>

  implicit val InetAddressUpdater = new Updater[InetAddress] {
    override def update(row: UpdatableRow, columnIndex: Int, x: InetAddress): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val PGIntervalUpdater = new Updater[PGInterval] {
    override def update(
      row: UpdatableRow,
      columnIndex: Int,
      x: PGInterval
    ): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val JsonUpdater = new Updater[JValue] {
    override def update(
      row: UpdatableRow,
      columnIndex: Int,
      x: JValue
    ): Unit = {
      row.updateString(columnIndex, JsonMethods.compact(JsonMethods.render(x)))
    }
  }

  implicit val LTreeUpdater = new Updater[LTree] {
    override def update(
      row: UpdatableRow,
      columnIndex: Int,
      x: LTree
    ): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val XmlUpdater = new Updater[Node] {
    override def update(
      row: UpdatableRow,
      columnIndex: Int,
      x: Node
    ): Unit = {
      row.updateString(columnIndex, x.toString)
    }
  }

}
