package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

trait HasPgTestingConfig {
  self: HasConfig =>

  def pgConfig: Config = pgDefaults.withFallback(config.getConfig("pg"))

  val pgDefaults = {
    val asString =
      """autoCommit = false
        |dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
      """.stripMargin

    ConfigFactory.parseString(asString)
  }

}
