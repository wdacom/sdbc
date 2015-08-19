package com.rocketfuel.sdbc.sqlserver.jdbc

import com.typesafe.config.{Config, ConfigFactory}
import com.rocketfuel.sdbc.config.TestingConfig

trait SqlTestingConfig {
  self: TestingConfig =>

  lazy val sqlTestCatalogPrefix: String = config.getString("testCatalogPrefix")

  def sqlConfigKey: String

  lazy val sqlRandomCatalog =
    ConfigFactory.parseString("catalog = " + testCatalogName)

  lazy val sqlConfig: Config =
    SqlTestingConfig.defaults.
    withFallback(config.getConfig(sqlConfigKey)).
    withFallback(sqlRandomCatalog)
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
