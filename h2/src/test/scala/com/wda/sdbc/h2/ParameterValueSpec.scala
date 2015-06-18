package com.wda.sdbc.h2

import java.sql.{Timestamp, Time, Date}
import java.util.UUID

import scalaz.Scalaz._

class ParameterValueSpec
  extends H2Suite {

  val uuid = UUID.randomUUID()

  testSelect[Int]("SELECT NULL", none[Int])

  testSelect[Byte]("SELECT CAST(1 AS tinyint)", 1.toByte.some)

  testSelect[Short]("SELECT CAST(1 AS smallint)", 1.toShort.some)

  testSelect[Int]("SELECT CAST(1 AS int)", 1.some)

  testSelect[Long]("SELECT CAST(1 AS bigint)", 1L.some)

  testSelect[String]("SELECT 'hello'", "hello".some)

  testSelect[Array[Byte]]("SELECT 0x0001ffa0", Array(0, 1, -1, -96).map(_.toByte).some)

  testSelect[Float]("SELECT CAST(3.14159 AS real)", 3.14159F.some)

  testSelect[Double]("SELECT CAST(3.14159 AS float)", 3.14159.some)

  testSelect[Boolean]("SELECT CAST(1 AS bit)", true.some)

  testSelect[BigDecimal]("SELECT CAST(3.14159 AS numeric(10,5)) --as Scala BigDecimal", BigDecimal("3.14159").some)

  testSelect[java.math.BigDecimal]("SELECT CAST(3.14159 AS numeric(10,5)) --as Java BigDecimal", BigDecimal("3.14159").underlying.some)

  testSelect[Date]("SELECT CAST('2014-12-29' AS date)", Date.valueOf("2014-12-29").some)

  testSelect[Time]("SELECT CAST('03:04:05' AS time) --as JDBC Time", Time.valueOf("03:04:05").some)

  testSelect[Timestamp]("SELECT CAST('2014-12-29 01:02:03.5' AS datetime)", Timestamp.valueOf("2014-12-29 01:02:03.5").some)

  testSelect[UUID](s"SELECT CAST('$uuid' AS uuid)", uuid.some)

}
