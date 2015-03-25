package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

import scala.util.Random

trait SqlTestingConfig {
  self: HasConfig =>

  lazy val sqlConfig: Config =
    SqlTestingConfig.defaults.
    withFallback(config.getConfig("sql")).
    withFallback(SqlTestingConfig.randomCatalog())
}

object SqlTestingConfig {
  val defaults = {
    val asString =
      """autoCommit = false
        |dataSourceClassName = "net.sourceforge.jtds.jdbcx.JtdsDataSource"
      """.stripMargin

    ConfigFactory.parseString(asString)
  }

  def randomCatalog() =
    ConfigFactory.parseString("catalog = sdbctest" + Random.nextInt(Int.MaxValue))
}
