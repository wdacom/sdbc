package com.rocketfuel

import com.typesafe.config.Config

trait HasConfig {
  def config: Config
}
