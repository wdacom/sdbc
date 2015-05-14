package com.wda.sdbc.jdbc

import com.typesafe.config.Config
import com.wda.sdbc.config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import java.sql.{Connection => JConnection}

import com.wda.sdbc.base

trait Pool extends base.Pool[HikariDataSource, java.sql.Connection] {
  self: DBMS =>

  trait IsJdbcPool extends base.Pool[JdbcPool, JConnection] {
    override def getConnection(pool: JdbcPool): JConnection = {
      pool.getConnection()
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

    def getConnection(): JConnection = {
      val connection = underlying.getConnection()
      initializeConnection(connection)
      connection
    }

    def withConnection[T](f: JConnection => T): T = {
      val connection = getConnection()
      try {
        f(connection)
      } finally {
        isConnection.closeQuietly(connection)
      }
    }

    def withTransaction[T](f: JConnection => T): T = {
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
