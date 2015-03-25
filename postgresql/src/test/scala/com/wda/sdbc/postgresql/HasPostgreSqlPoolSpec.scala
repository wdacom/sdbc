package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._
import com.wda.sdbc.config.{PgTestingConfig, TestingConfig}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class HasPostgreSqlPoolSpec
  extends FunSuite
  with HasPostgreSqlPool
  with TestingConfig
  with PgTestingConfig
  with BeforeAndAfterAll {

  def testDatabaseExists(): Boolean = {
    withPgMaster[Boolean] { implicit connection =>
      Select[Boolean]("SELECT EXISTS(SELECT * FROM pg_database WHERE datname = $databaseName)").on("databaseName" -> pgTestCatalogName).single()
    }
  }

  test("creates and destroys test database") {

    pgBeforeAll()

    assert(testDatabaseExists())

    pgDropTestCatalog()

    assert(! testDatabaseExists())

  }

  override protected def afterAll(): Unit = {
    pgMasterPool.shutdown()
  }
}
