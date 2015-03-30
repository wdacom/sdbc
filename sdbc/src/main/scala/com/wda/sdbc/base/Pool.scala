package com.wda.sdbc.base

import com.typesafe.config.Config
import com.wda.sdbc.{DBMS, config}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

trait Pool {
  self: DBMS =>

  case class Pool(configuration: HikariConfig) {

    if (DBMS.of(configuration).getClass != self.getClass) {
      throw new IllegalArgumentException("HikariConfig is for the wrong DBMS.")
    }

    //Set the test query if the driver doesn't support .isValid().
    if (! self.supportsIsValid) {
      configuration.setConnectionTestQuery("SELECT 1")
    }

    val underlying = new HikariDataSource(configuration)

    implicit val dbms: DBMS = self

    def getConnection(): Connection = {
      val connection = underlying.getConnection()
      initializeConnection(connection)
      Connection(connection)
    }

    def withConnection[T](f: Connection => T): T = {
      val connection = getConnection()
      try {
        f(connection)
      } finally {
        connection.closeQuietly()
      }
    }

    def withTransaction[T](f: Connection => T): T = {
      val connection = getConnection()
      connection.setAutoCommit(false)
      try {
        val result = f(connection)
        connection.commit()
        result
      } finally {
        connection.closeQuietly()
      }
    }

  }

  object Pool {
    import config._

    def apply(config: Config): Pool = {
      Pool(config.toHikariConfig)
    }
  }

  implicit def PoolToHikariDataSource(pool: Pool): HikariDataSource = {
    pool.underlying
  }

}
