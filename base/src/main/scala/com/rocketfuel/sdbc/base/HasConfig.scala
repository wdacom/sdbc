package com.rocketfuel.sdbc.base

import com.typesafe.config.Config

trait HasConfig {
  def config: Config
}
