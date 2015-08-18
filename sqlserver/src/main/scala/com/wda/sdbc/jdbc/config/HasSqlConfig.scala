package com.wda.sdbc.jdbc.config

import com.typesafe.config.Config

trait HasSqlConfig {
  def sqlConfig: Config
}
