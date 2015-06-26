package com.wda.sdbc.postgresql

import java.sql.{Array => _, _}
import java.util.UUID

import org.joda.time._
import org.json4s.JValue
import org.json4s.jackson.JsonMethods
import org.postgresql.util.PGInterval

import scalaz.Scalaz._
import com.wda.sdbc.PostgreSql._

class GetterSpec
  extends PostgreSqlSuite {

  override protected def beforeAll(): Unit = {
    pgBeforeAll()
    createLTree()
  }

  val jsonString = """{"hi":"there"}"""

  val uuid = UUID.randomUUID()

  testSelect[Int]("SELECT NULL", none[Int])

  testSelect[Short]("SELECT 1::smallint", 1.toShort.some)

  testSelect[Int]("SELECT 1::integer", 1.some)

  testSelect[Long]("SELECT 1::bigint", 1L.some)

  testSelect[String]("SELECT 'hello'::text", "hello".some)

  testSelect[Array[Byte]]("SELECT E'\\\\x0001ffa0'::bytea", Array(0, 1, -1, -96).map(_.toByte).some)

  testSelect[Float]("SELECT 3.14159::real", 3.14159F.some)

  testSelect[Double]("SELECT 3.14159::double precision", 3.14159.some)

  testSelect[Boolean]("SELECT true", true.some)

  testSelect[BigDecimal]("SELECT 3.14159::numeric(10,5) --as Scala BigDecimal", BigDecimal("3.14159").some)

  testSelect[java.math.BigDecimal]("SELECT 3.14159::numeric(10,5) --as Java BigDecimal", BigDecimal("3.14159").underlying.some)

  testSelect[scala.xml.Node]("SELECT '<a>hi</a>'::xml", <a>hi</a>.some)

  testSelect[Date]("SELECT '2014-12-29'::date", Date.valueOf("2014-12-29").some)

  testSelect[Time]("SELECT '03:04:05'::time --as JDBC Time", Time.valueOf("03:04:05").some)

  testSelect[Timestamp]("SELECT '2014-12-29 01:02:03.5'::timestamp --as JDBC Timestamp", Timestamp.valueOf("2014-12-29 01:02:03.5").some)

  testSelect[PGInterval]("SELECT '9 years 11 mons 29 days 11:02:13.154936'::interval --as PGInterval", new PGInterval("9 years 11 mons 29 days 11:02:13.154936").some)

  testSelect[LTree]("SELECT 'a.b.c'::ltree", LTree("a", "b", "c").some)

  testSelect[UUID](s"SELECT '$uuid'::uuid", uuid.some)

  testSelect[JValue](s"SELECT '$jsonString'::json", JsonMethods.parse(jsonString).some)

  testSelect[JValue](s"SELECT '$jsonString'::jsonb", JsonMethods.parse(jsonString).some)

  testSelect[LocalDateTime]("SELECT '2014-12-29 01:02:03.5'::timestamp --as Java 8 LocalDateTime", LocalDateTime.parse("2014-12-29T01:02:03.5").some)

  {
    //Convert the time being tested into UTC time
    //using the current time zone's offset at the time
    //that we're testing.
    //We can't use the current offset, because of, for example,
    //daylight savings.
    val localTime = LocalDateTime.parse("2014-12-29T01:02:03.5")
    val offset = DateTimeZone.getDefault()
    val expectedTime = new Instant(localTime.toDateTime(offset))
    testSelect[Instant]("SELECT '2014-12-29 01:02:03.5'::timestamp --as Java 8 Instant", expectedTime.some)
  }

  testSelect[DateTime]("SELECT '2014-12-29 01:02:03.5-4'::timestamp with time zone --as Java 8 OffsetDateTime", DateTime.parse("2014-12-29T01:02:03.5-04:00").some)

  testSelect[Instant]("SELECT '2014-12-29 01:02:03.5-4'::timestamp with time zone --as Java 8 Instant", Instant.parse("2014-12-29T05:02:03.5Z").some)

  testSelect[PGInterval]("SELECT '9 years 11 mons 29 days 11:02:13.154936'::interval --as PGInterval", new PGInterval("9 years 11 mons 29 days 11:02:13.154936").some)

  testSelect[Duration]("SELECT '9 years 11 mons 29 days 11:02:13.154936'::interval --as Java 8 Duration", Some[Duration](new PGInterval("9 years 11 mons 29 days 11:02:13.154936")))

}
