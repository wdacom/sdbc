package com.wda

import com.typesafe.config.Config

trait HasConfig {
  def config: Config
}
