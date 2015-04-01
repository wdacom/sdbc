package com.wda.sdbc.config

import scala.util.Random

trait TestingConfig extends HasConfig {
  def testCatalogPrefix: String = config.getString("testCatalogPrefix")

  val testCatalogSuffix: String = Random.nextInt(Int.MaxValue).toString

  def testCatalog: String = testCatalogPrefix + testCatalogSuffix
}
