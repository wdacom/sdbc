package com.rocketfuel.sdbc.sqlserver.jdbc

import java.sql.{Time, Date, Timestamp}
import org.joda.time.{LocalTime, LocalDate, Instant}
import java.util.{Calendar, UUID}

import com.rocketfuel.sdbc.base.jdbc.Updater
import scodec.bits.ByteVector

import scala.reflect.ClassTag

class UpdatersSpec extends SqlServerSuite {

  def testUpdate[T](typeName: String)(before: T)(after: T)(implicit ctag: ClassTag[T], updater: Updater[T], converter: Row => T): Unit = {
    test(s"Update ${ctag.runtimeClass.getName}") {implicit connection =>
      Update(s"CREATE TABLE tbl (id int identity PRIMARY KEY, v $typeName)").update()

      update"INSERT INTO tbl (v) VALUES ($before)".update()

      for (row <- selectForUpdate"SELECT * FROM tbl".iterator()) {
        row("v") = after
        row.updateRow()
      }

      val maybeValue = Select[T]("SELECT v FROM tbl").option()

      assert(maybeValue.nonEmpty)

      assert(maybeValue.nonEmpty)

      val actualValue = maybeValue.get

      //Java's java.sql.Time and java.sql.Date equality sucks
      after match {
        case _: java.sql.Time =>
          val actualCalendar = Calendar.getInstance()
          actualCalendar.setTime(actualValue.asInstanceOf[java.sql.Time])
          val expectedCalendar = Calendar.getInstance()
          expectedCalendar.setTime(after.asInstanceOf[java.sql.Time])
          assertResult(expectedCalendar.get(Calendar.HOUR))(actualCalendar.get(Calendar.HOUR))
          assertResult(expectedCalendar.get(Calendar.MINUTE))(actualCalendar.get(Calendar.MINUTE))
          assertResult(expectedCalendar.get(Calendar.SECOND))(actualCalendar.get(Calendar.SECOND))
        case _: java.sql.Date =>
          val actualCalendar = Calendar.getInstance()
          actualCalendar.setTime(actualValue.asInstanceOf[java.sql.Date])
          val expectedCalendar = Calendar.getInstance()
          expectedCalendar.setTime(after.asInstanceOf[java.sql.Date])
          assertResult(expectedCalendar.get(Calendar.YEAR))(actualCalendar.get(Calendar.YEAR))
          assertResult(expectedCalendar.get(Calendar.MONTH))(actualCalendar.get(Calendar.MONTH))
          assertResult(expectedCalendar.get(Calendar.DAY_OF_MONTH))(actualCalendar.get(Calendar.DAY_OF_MONTH))
        case _ =>
          assertResult(after)(actualValue)
      }
    }
  }

  testUpdate[Long]("bigint")(1L)(2L)

  testUpdate[Int]("int")(1)(2)

  testUpdate[Short]("smallint")(1.toShort)(2.toShort)

  testUpdate[Byte]("tinyint")(1.toByte)(2.toByte)

  testUpdate[Double]("float")(1.0)(2.0)

  testUpdate[Float]("real")(1.0F)(2.0F)

  testUpdate[java.lang.Long]("bigint")(1L)(2L)

  testUpdate[java.lang.Integer]("int")(1)(2)

  testUpdate[java.lang.Short]("smallint")(1.toShort)(2.toShort)

  testUpdate[java.lang.Byte]("tinyint")(1.toByte)(2.toByte)

  testUpdate[java.lang.Double]("float")(1.0)(2.0)

  testUpdate[java.lang.Float]("real")(1.0F)(2.0F)

  testUpdate[ByteVector]("varbinary(max)")(ByteVector(1, 2, 3))(ByteVector(4, 5, 6))

  testUpdate[BigDecimal]("decimal")(BigDecimal(3))(BigDecimal("500"))

  testUpdate[Timestamp]("datetime2")(new Timestamp(0))(new Timestamp(1442411519163L))

  testUpdate[Date]("date")(new Date(0))(new Date(1442411519164L))

  testUpdate[Time]("time")(new Time(0))(new Time(1000))

  testUpdate[Instant]("datetime2")(new Instant(0))(Instant.parse("2015-09-17T19:29:57.697Z"))

  testUpdate[LocalDate]("date")(new LocalDate(0))(LocalDate.now())

  testUpdate[LocalTime]("time")(new LocalTime(0))(LocalTime.now())

  testUpdate[Boolean]("bit")(false)(true)

  testUpdate[String]("varchar(max)")("hi")("bye")

  testUpdate[UUID]("uniqueidentifier")(UUID.randomUUID())(UUID.randomUUID())

  test(s"Update HierarchyId") {implicit connection =>
    val before = HierarchyId()
    val after = HierarchyId(1, 2)

    Update(s"CREATE TABLE tbl (id int identity PRIMARY KEY, v hierarchyid)").update()

    update"INSERT INTO tbl (v) VALUES ($before)".update()

    for (row <- selectForUpdate"SELECT id, v FROM tbl".iterator()) {
      row("v") = after
      row.updateRow()
    }

    val maybeValue = Select[HierarchyId]("SELECT v.ToString() FROM tbl").option()

    assert(maybeValue.nonEmpty)

    assertResult(Some(after))(maybeValue)
  }

  test(s"Update None") {implicit connection =>
    val before = Some(1)
    val after = None

    Update(s"CREATE TABLE tbl (id int identity PRIMARY KEY, v int)").update()

    update"INSERT INTO tbl (v) VALUES ($before)".update()

    for (row <- selectForUpdate"SELECT id, v FROM tbl".iterator()) {
      row("v") = after
      row.updateRow()
    }

    val maybeRow = Select[Option[Int]]("SELECT v FROM tbl").iterator.toStream.headOption

    assert(maybeRow.nonEmpty, "There was a row")

    val maybeValue = maybeRow.get

    assert(maybeValue.isEmpty)
  }

}
