package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

import scala.util.Random

trait PgTestingConfig {
  self: HasConfig =>

  def pgTestCatalogPrefix: String = config.getString("testCatalogPrefix")

  def pgRandomCatalog() =
    ConfigFactory.parseString("dataSource.databaseName = " + pgTestCatalogPrefix + Random.nextInt(Int.MaxValue))

  lazy val pgConfig: Config =
    PgTestingConfig.defaults.
    withFallback(config.getConfig("pg")).
    withFallback(pgRandomCatalog())

}

object PgTestingConfig {

  val defaults = {
    val asString =
      """autoCommit = false
        |dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
      """.stripMargin

    ConfigFactory.parseString(asString)
  }

}
