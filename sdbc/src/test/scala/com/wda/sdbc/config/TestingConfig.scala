package com.wda.sdbc.config

import com.typesafe.config.{ConfigFactory, Config}

trait TestingConfig extends HasConfig {
  override def config: Config = ConfigFactory.load("sql-testing.conf")
}
