package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

import scala.util.Random

trait PgTestingConfig {
  self: HasConfig =>

  lazy val pgConfig: Config =
    PgTestingConfig.defaults.
    withFallback(config.getConfig("pg")).
    withFallback(PgTestingConfig.randomCatalog())

}

object PgTestingConfig {
  val defaults = {
    val asString =
      """autoCommit = false
        |dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
      """.stripMargin

    ConfigFactory.parseString(asString)
  }
  
  def randomCatalog() =
    ConfigFactory.parseString("dataSource.databaseName = sdbctest" + Random.nextInt(Int.MaxValue))
}
