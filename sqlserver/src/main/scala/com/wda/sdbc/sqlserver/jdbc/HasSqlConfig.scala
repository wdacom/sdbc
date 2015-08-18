package com.wda.sdbc.sqlserver.jdbc

import com.typesafe.config.Config

trait HasSqlConfig {
  def sqlConfig: Config
}
