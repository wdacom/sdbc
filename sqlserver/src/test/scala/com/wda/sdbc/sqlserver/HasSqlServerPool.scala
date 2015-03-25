package com.wda.sdbc.sqlserver

import com.wda.sdbc.SqlServer._
import com.wda.sdbc.config._

trait HasSqlServerPool {
  self: SqlTestingConfig =>

  val sqlTestCatalogName = sqlConfig.getString("catalog")

  protected var sqlPool: Option[Pool] = None

  protected lazy val sqlMasterPool: Pool = {
    val masterConfig = sqlConfig.toHikariConfig
    masterConfig.setMaximumPoolSize(1)
    masterConfig.setCatalog("master")

   Pool(masterConfig)
  }

  protected def withSqlMaster[T](f: Connection => T): T = {
    val connection = sqlMasterPool.getConnection
    try {
      f(connection)
    } finally {
      if (!connection.isClosed) {
        connection.close()
      }
    }
  }

  protected def sqlCreateTestCatalog(): Unit = {
    if (sqlPool.isEmpty) {
      withSqlMaster { implicit connection =>
        Update(s"CREATE DATABASE ${Identifier.quote(sqlTestCatalogName)};").execute()
      }

      sqlPool = Some(Pool(sqlConfig))
    }
  }

  protected def sqlDropTestCatalog(): Unit = {
    sqlPool.foreach(_.shutdown())
    sqlPool = None

    val dropText =
      s"""ALTER DATABASE ${Identifier.quote(sqlTestCatalogName)}
          |  SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
          |
          |DROP DATABASE ${Identifier.quote(sqlTestCatalogName)};
      """.stripMargin

    val drop = Update(dropText)

    withSqlMaster { implicit connection =>
      drop.execute()
    }
  }

  def withSql[T](f: Connection => T): T = {
    val connection = sqlPool.get.getConnection
    try {
      f(connection)
    } finally {
      if (! connection.isClosed) {
        connection.close()
      }
    }
  }

  def sqlBeforeAll(): Unit = {
    sqlCreateTestCatalog()
  }

  def sqlAfterAll(): Unit = {
    sqlDropTestCatalog()
    sqlMasterPool.shutdown()
  }

}
