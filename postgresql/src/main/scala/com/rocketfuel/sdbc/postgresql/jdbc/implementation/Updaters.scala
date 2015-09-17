package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.util.UUID
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc.Cidr
import org.json4s._
import org.postgresql.util.PGobject
import scala.xml.Node
import org.joda.time.{Duration => JodaDuration, DateTime}
import scala.concurrent.duration.{Duration => ScalaDuration}

trait Updaters
  extends LongUpdater
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
  with UpdateReader {
  self: PGTimestampTzImplicits
    with IntervalImplicits
    with PGInetAddressImplicits
    with PGJsonImplicits
    with PGLocalTimeImplicits =>

  private def IsPGobjectUpdater[T](implicit converter: T => PGobject): Updater[T] = {
    new Updater[T] {
      override def update(row: UpdatableRow, columnIndex: Int, x: T): Unit = {
        PGobjectUpdater.update(row, columnIndex, converter(x))
      }
    }
  }

  implicit val DateTimeUpdater = IsPGobjectUpdater[DateTime]

  implicit val ScalaDurationUpdater = IsPGobjectUpdater[ScalaDuration]

  implicit val JodaDurationUpdater = IsPGobjectUpdater[JodaDuration]

  implicit val JValueUpdater = IsPGobjectUpdater[JValue]

  implicit val InetAddressUpdater = IsPGobjectUpdater[InetAddress]

  implicit val CidrUpdater = IsPGobjectUpdater[Cidr]

  implicit val PGobjectUpdater = new Updater[PGobject] {
    override def update(
      row: UpdatableRow,
      columnIndex: Int,
      x: PGobject
    ): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  override implicit val UUIDUpdater: Updater[UUID] = new Updater[UUID] {
    override def update(row: UpdatableRow, columnIndex: Int, x: UUID): Unit = {
      row.updateObject(columnIndex, x)
    }
  }

  implicit val XmlUpdater = new Updater[Node] {
    override def update(
      row: UpdatableRow,
      columnIndex: Int,
      x: Node
    ): Unit = {
      val connection = row.underlying.getStatement.getConnection
      val sqlXml = connection.createSQLXML()
      sqlXml.setString(x.toString())
      row.updateSQLXML(columnIndex, sqlXml)
    }
  }

  implicit val MapUpdater = new Updater[Map[String, String]] {
    override def update(row: UpdatableRow, columnIndex: Int, x: Map[String, String]): Unit = {
      import scala.collection.convert.decorateAsJava._
      row.updateObject(columnIndex, x.asJava)
    }
  }

}
