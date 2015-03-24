package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._
import com.wda.sdbc.config._
import org.scalatest.{BeforeAndAfterAll, Suite}

trait HasPostgreSqlPool {
  this: HasPgTestingConfig =>

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
  protected lazy val pgMasterPool = {
    val masterConfig = pgConfig.toHikariConfig
    masterConfig.getDataSourceProperties.setProperty("databaseName", "postgres")
    masterConfig.setMaximumPoolSize(1)

    Pool(masterConfig)
  }

  protected def withPgMaster[T](f: Connection => T): T = {
    val connection = pgMasterPool.getConnection
    try {
      f(connection)
    } finally {
      connection.close()
    }
  }

  protected def pgCreateTestCatalog(): Unit = {
    if (pgPool.isEmpty) {
      withPgMaster { implicit connection =>
        connection.setAutoCommit(true)
        createTestCatalog.execute()
      }

      pgPool = Some(Pool(pgConfig))
    }
  }

  protected def pgDropTestCatalog(): Unit = {
    pgPool.foreach(_.shutdown())
    pgPool = None

    withPgMaster { implicit connection =>
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

  def createLTree(): Unit = {
    withPg { implicit connection =>
      Update("CREATE EXTENSION ltree;").execute()
      connection.commit()
    }
  }

  protected def pgBeforeAll(): Unit = {
    pgCreateTestCatalog()
  }

  protected def pgAfterAll(): Unit = {
    pgDropTestCatalog()
    pgMasterPool.shutdown()
  }
}
