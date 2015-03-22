package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._
import com.wda.sdbc.config._
import org.scalatest.{BeforeAndAfterAll, Suite}

trait HasPostgreSqlPool extends BeforeAndAfterAll {
  this: Suite with HasPgTestingConfig =>

  lazy val testCatalogName = pgConfig.getString("dataSource.databaseName")

  lazy val dropTestCatalog: Update = {
    val dropText =
      s"""SELECT pg_terminate_backend(pid)
         |FROM pg_stat_activity
         |WHERE pg_stat_activity.datname = $$databaseName
         |  AND pid <> pg_backend_pid();
         |
         |DROP DATABASE IF EXISTS ${Identifier.quote(testCatalogName)};
      """.stripMargin

    Update(dropText).on("databaseName" -> testCatalogName)
  }

  lazy val createTestCatalog =
    Update(s"CREATE DATABASE ${Identifier.quote(testCatalogName)};")

  private var pgPool: Option[Pool] = None

  /*
  PostgreSQL doesn't allow changing the database for a connection,
  so we need a separate connection for the postgres database.
   */
  private lazy val postgresMasterPool = {
    val masterConfig = pgConfig.toHikariConfig
    masterConfig.getDataSourceProperties.setProperty("databaseName", "postgres")
    masterConfig.setMaximumPoolSize(1)

    Pool(masterConfig)
  }

  protected def withPostgresMaster[T](f: Connection => T): T = {
    val connection = postgresMasterPool.getConnection
    try {
      f(connection)
    } finally {
      connection.close()
    }
  }

  private def postgresCreateTestCatalog(): Unit = {
    withPostgresMaster { implicit connection =>
      connection.setAutoCommit(true)
      createTestCatalog.execute()
    }

    val testConfig = pgConfig.toHikariConfig
    testConfig.setAutoCommit(false)

    pgPool = Some(Pool(testConfig))

    withPg { implicit connection =>
      Update("CREATE EXTENSION ltree;").execute()
      connection.commit()
    }
  }

  private def postgresDropTestCatalog(): Unit = {
    pgPool.map(_.shutdown())
    pgPool = None

    withPostgresMaster { implicit connection =>
      connection.setAutoCommit(true)
      dropTestCatalog.execute()
    }
  }

  def withPg[T](f: Connection => T): T = {
    val connection = pgPool.get.getConnection
    try {
      f(connection)
    } finally {
      connection.close()
    }
  }

  override protected def beforeAll(): Unit = {
    postgresDropTestCatalog()
    postgresCreateTestCatalog()
  }

  override protected def afterAll(): Unit = {
    postgresDropTestCatalog()
    postgresMasterPool.shutdown()
  }
}
