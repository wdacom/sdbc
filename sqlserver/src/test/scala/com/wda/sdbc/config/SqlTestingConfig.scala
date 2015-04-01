package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

import scala.util.Random

trait SqlTestingConfig {
  self: HasConfig =>

  def sqlTestCatalogPrefix: String = config.getString("testCatalogPrefix")

  def sqlRandomCatalog() =
    ConfigFactory.parseString("catalog = " + sqlTestCatalogPrefix + Random.nextInt(Int.MaxValue))

  def sqlConfigKey: String

  lazy val sqlConfig: Config =
    SqlTestingConfig.defaults.
    withFallback(config.getConfig(sqlConfigKey)).
    withFallback(sqlRandomCatalog())
}

object SqlTestingConfig {

  val defaults = {
    val asString =
      """autoCommit = false
        |dataSourceClassName = "net.sourceforge.jtds.jdbcx.JtdsDataSource"
      """.stripMargin

    ConfigFactory.parseString(asString)
  }

}
