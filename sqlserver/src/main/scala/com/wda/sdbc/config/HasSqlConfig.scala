package com.wda.sdbc.config

import com.typesafe.config.Config

trait HasSqlConfig {
  def sqlConfig: Config
}
