package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._
import com.wda.sdbc.config._
import org.scalatest.{BeforeAndAfterAll, Suite}

trait HasPostgreSqlPool {
  this: PgTestingConfig =>

  val pgTestCatalogName = pgConfig.getString("dataSource.databaseName")

  private var pgPool: Option[Pool] = None

  /*
  PostgreSQL doesn't allow changing the database for a connection,
  so we need a separate connection for the postgres database.
   */
  protected val pgMasterPool = {
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

        Update(s"CREATE DATABASE ${Identifier.quote(pgTestCatalogName)};").execute()
      }
      pgPool = Some(Pool(pgConfig))
    }
  }

  protected def pgDropTestCatalogs(): Unit = {
    pgPool.foreach(_.shutdown())
    pgPool = None

    withPgMaster { implicit connection =>
      connection.setAutoCommit(true)

      val databases =
        Select[String]("SELECT datname FROM pg_database WHERE datname LIKE $catalogPrefix").
        on("catalogPrefix" -> (pgTestCatalogPrefix + "%")).
        seq()

      for (database <- databases) {
        util.Try {
          Update(
            """SELECT pg_terminate_backend(pid)
              |FROM pg_stat_activity
              |WHERE pg_stat_activity.datname = $databaseName
              |AND pid <> pg_backend_pid();
            """.stripMargin
          ).on("databaseName" -> database).execute()

          Update("DROP DATABASE " + Identifier.quote(database)).execute()
        }
      }
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

  /**
   * Method for use with ScalaTest's beforeEach().
   */
  protected def pgBeforeEach(): Unit = {
    pgCreateTestCatalog()
  }

  /**
   * Method for use with ScalaTest's afterEach().
   */
  protected def pgAfterEach(): Unit = {
    pgDropTestCatalogs()
  }

  /**
   * Method for use with ScalaTest's beforeAll().
   */
  protected def pgBeforeAll(): Unit = {
    pgCreateTestCatalog()
  }

  /**
   * Method for use with ScalaTest's afterAll().
   */
  protected def pgAfterAll(): Unit = {
    pgDropTestCatalogs()
    pgMasterPool.shutdown()
  }
}
