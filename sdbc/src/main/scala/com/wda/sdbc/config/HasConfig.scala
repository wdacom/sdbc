package com.wda.sdbc.config

import com.typesafe.config.Config

trait HasConfig {
  def config: Config
}
