package com.wda.sdbc.jdbc

import com.typesafe.config.Config
import com.wda.sdbc.base.Closable
import com.wda.sdbc.config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

import com.wda.sdbc.base

trait Pool extends base.Pool[HikariDataSource, java.sql.Connection] {
  self: DBMS =>

  implicit val closableConnection: Closable[java.sql.Connection] =
    new Closable[java.sql.Connection] {
      override def close(connection: UnderlyingConnection): Unit = {
        connection.close()
      }
    }

  case class JdbcPool(configuration: HikariConfig) {

    if (DBMS.of(configuration).getClass != self.getClass) {
      throw new IllegalArgumentException("HikariConfig is for the wrong DBMS.")
    }

    //Set the test query if the driver doesn't support .isValid().
    if (! self.supportsIsValid) {
      configuration.setConnectionTestQuery("SELECT 1")
    }

    val underlying = new HikariDataSource(configuration)

    implicit val dbms: DBMS = self

    def getConnection(): UnderlyingConnection = {
      val connection = underlying.getConnection()
      initializeConnection(connection)
      connection
    }

    def withConnection[T](f: UnderlyingConnection => T): T = {
      val connection = getConnection()
      try {
        f(connection)
      } finally {
        isConnection.closeQuietly(connection)
      }
    }

    def withTransaction[T](f: UnderlyingConnection => T): T = {
      val connection = getConnection()
      connection.setAutoCommit(false)
      try {
        val result = f(connection)
        connection.commit()
        result
      } finally {
        isConnection.closeQuietly(connection)
      }
    }

  }

  object JdbcPool {
    import config._

    def apply(config: Config): JdbcPool = {
      JdbcPool(config.toHikariConfig)
    }
  }

  implicit def PoolToHikariDataSource(pool: JdbcPool): HikariDataSource = {
    pool.underlying
  }

}
