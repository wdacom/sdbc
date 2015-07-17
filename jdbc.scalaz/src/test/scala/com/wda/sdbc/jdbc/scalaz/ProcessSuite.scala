package com.wda.sdbc.jdbc.scalaz

import com.wda.sdbc.h2.H2Suite
import com.wda.sdbc.H2._
import com.zaxxer.hikari.HikariConfig
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import scalaz.stream._

abstract class ProcessSuite
  extends H2Suite
  with BeforeAndAfterEach
  with BeforeAndAfterAll {

  val pool = {
    val poolConfig = new HikariConfig()
    poolConfig.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")

    Pool(poolConfig)
  }

  val expectedCount = 100L

  val insertSet = 0L.until(expectedCount).toSet

  val inserts = {
    val integers = Process.emitAll(insertSet.toSeq)

    val insert = Update("INSERT INTO tbl (i) VALUES ($i)")

    integers.map(i => insert.on("i" -> i)).toSource
  }

  val select = Select[Int]("SELECT i FROM tbl")

  override protected def beforeEach(): Unit = {
    withMemConnection(name = "test", dbCloseDelay = None) { implicit connection: Connection =>
      Update("CREATE TABLE tbl (i bigint PRIMARY KEY)").execute()
    }
  }

  override protected def afterEach(): Unit = {
    withMemConnection(name = "test", dbCloseDelay = None) { implicit connection: Connection =>
      Update("DROP TABLE tbl").execute()
    }
  }

  override protected def afterAll(): Unit = {
    pool.shutdown()
  }

}
