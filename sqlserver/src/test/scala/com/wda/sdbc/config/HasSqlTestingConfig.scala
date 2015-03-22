package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

trait HasSqlTestingConfig {
  self: HasConfig =>

  def sqlConfig: Config = sqlDefaults.withFallback(config.getConfig("sql"))

  val sqlDefaults = {
    val asString =
      """autoCommit = false
        |dataSourceClassName = "net.sourceforge.jtds.jdbcx.JtdsDataSource"
      """.stripMargin

    ConfigFactory.parseString(asString)
  }
}
