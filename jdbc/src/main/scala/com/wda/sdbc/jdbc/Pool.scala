package com.wda.sdbc.jdbc

import java.sql.{Connection => JConnection}

import com.typesafe.config.Config
import com.wda.sdbc.base
import com.zaxxer.hikari.pool.HikariPool
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

trait Pool extends base.Pool[HikariDataSource, java.sql.Connection] {
  self: DBMS =>

  import HikariCP._

  case class Pool(configuration: HikariConfig)
    extends base.Pool[HikariPool, JConnection] {

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
      underlying.getConnection()
    }

    def withTransaction[T](f: JConnection => T): T = {
      withConnection[T] { connection =>
        connection.setAutoCommit(false)
        val result = f(connection)
        connection.commit()
        result
      }
    }

  }

  object Pool {
    def apply(config: Config): Pool = {
      Pool(config.toHikariConfig)
    }
  }

  implicit def PoolToHikariDataSource(pool: Pool): HikariDataSource = {
    pool.underlying
  }

}
