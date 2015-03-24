package com.wda.sdbc.sqlserver

import com.wda.sdbc.SqlServer._
import com.wda.sdbc.config._
import org.scalatest.{BeforeAndAfterAll, Suite}

trait HasSqlServerPool extends BeforeAndAfterAll {
  self: Suite with HasSqlTestingConfig =>

  protected var sqlPool: Option[Pool] = None

  protected lazy val sqlMasterPool: Pool = {
    val masterConfig = sqlConfig.toHikariConfig
    masterConfig.setMaximumPoolSize(1)
    masterConfig.setCatalog("master")

   Pool(masterConfig)
  }

  protected def withMaster[T](f: Connection => T): T = {
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
      withMaster { implicit connection =>
        HasSqlServerPool.create()
      }

      sqlPool = Some(Pool(sqlConfig))
    }
  }

  protected def sqlDropTestCatalog(): Unit = {
    sqlPool.foreach(_.shutdown())
    sqlPool = None

    withMaster { implicit connection =>
      HasSqlServerPool.drop()
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

  override protected def beforeAll(): Unit = {
    sqlCreateTestCatalog()
  }

  override protected def afterAll(): Unit = {
    sqlDropTestCatalog()
    sqlMasterPool.shutdown()
  }
}

object HasSqlServerPool extends AbstractDeployable {

  val dropText =
    """ALTER DATABASE test
      |  SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
      |
      |DROP DATABASE test;
    """.stripMargin

  val createText = "CREATE DATABASE test;"

  override val createStatements: Iterable[Update] = Iterable(Update(createText))

  override val dropStatements: Iterable[Update] = Iterable(Update(dropText))

}
