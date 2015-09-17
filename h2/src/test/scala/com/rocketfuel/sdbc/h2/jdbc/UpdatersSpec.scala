package com.rocketfuel.sdbc.h2.jdbc

import java.sql.{Time, Date, Timestamp}
import org.joda.time._
import java.util.{Calendar, UUID}

import com.rocketfuel.sdbc.base.jdbc.Updater
import scodec.bits.ByteVector

import scala.reflect.ClassTag

class UpdatersSpec extends H2Suite {

  def testUpdate[T](typeName: String)(before: T)(after: T)(implicit ctag: ClassTag[T], updater: Updater[T], converter: Row => T): Unit = {
    test(s"Update ${ctag.runtimeClass.getName}") {implicit connection =>
      Update(s"CREATE TABLE tbl (id identity PRIMARY KEY, v $typeName)").update()

      update"INSERT INTO tbl (v) VALUES ($before)".update()

      for (row <- selectForUpdate"SELECT * FROM tbl".iterator()) {
        row("v") = after
        row.updateRow()
      }

      val maybeValue = Select[T]("SELECT v FROM tbl").option()

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

  testUpdate[Long]("int8")(1L)(2L)

  testUpdate[Int]("int4")(1)(2)

  testUpdate[Short]("int2")(1.toShort)(2.toShort)

  testUpdate[Byte]("tinyint")(1.toByte)(2.toByte)

  testUpdate[Double]("float8")(1.0)(2.0)

  testUpdate[Float]("float4")(1.0F)(2.0F)

  testUpdate[java.lang.Long]("int8")(1L)(2L)

  testUpdate[java.lang.Integer]("int4")(1)(2)

  testUpdate[java.lang.Short]("int2")(1.toShort)(2.toShort)

  testUpdate[java.lang.Byte]("tinyint")(1.toByte)(2.toByte)

  testUpdate[java.lang.Double]("float8")(1.0)(2.0)

  testUpdate[java.lang.Float]("float4")(1.0F)(2.0F)

  testUpdate[ByteVector]("bytea")(ByteVector(1, 2, 3))(ByteVector(4, 5, 6))

  testUpdate[BigDecimal]("numeric")(BigDecimal(3))(BigDecimal("500"))

  testUpdate[Timestamp]("timestamp")(new Timestamp(0))(new Timestamp(1000))

  testUpdate[Date]("date")(new Date(0))(new Date(DateTime.now().getMillis))

  testUpdate[Time]("time")(new Time(0))(new Time(DateTime.now().getMillis))

  testUpdate[Instant]("timestamp")(new Instant(0))(Instant.now())

  testUpdate[LocalDate]("date")(new LocalDate(0))(LocalDate.now())

  //H2 doesn't store fractional seconds.
  testUpdate[LocalTime]("time")(new LocalTime(0))(new LocalTime(1000))

  testUpdate[Boolean]("bool")(false)(true)

  testUpdate[String]("text")("hi")("bye")

  testUpdate[UUID]("uuid")(UUID.randomUUID())(UUID.randomUUID())

  test(s"Update None") {implicit connection =>
    val before = Some(1)
    val after = None

    Update(s"CREATE TABLE tbl (id identity PRIMARY KEY, v int)").update()

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
