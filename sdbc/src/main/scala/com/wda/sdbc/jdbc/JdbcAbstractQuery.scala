package com.wda.sdbc.jdbc

import java.sql.{Connection => JConnection, SQLException, PreparedStatement}

import com.wda.sdbc.base

trait JdbcAbstractQuery extends base.AbstractQuery {
  self: JdbcConnection with JdbcParameterValue =>

  abstract class JdbcAbstractQuery[Self <: AbstractQuery[Self]] extends AbstractQuery[Self] {

  }
  
}
