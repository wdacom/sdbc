package com.wda.sdbc.sqlserver.jdbc.implementation

import com.typesafe.config.Config

trait HasSqlConfig {
  def sqlConfig: Config
}
