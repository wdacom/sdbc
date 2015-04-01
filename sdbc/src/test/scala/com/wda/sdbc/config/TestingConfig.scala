package com.wda.sdbc.config

import scala.util.Random

trait TestingConfig extends HasConfig {
  def testCatalogPrefix: String = config.getString("testCatalogPrefix")

  private lazy val randomInt = Random.nextInt(Int.MaxValue)

  def testCatalogSuffix: String = randomInt.toString

  def testCatalogName: String = testCatalogPrefix + testCatalogSuffix
}
