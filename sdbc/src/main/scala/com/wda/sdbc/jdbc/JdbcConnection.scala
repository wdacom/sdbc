package com.wda.sdbc.jdbc

import com.wda.sdbc.base
import java.sql.{Connection => JConnection, PreparedStatement => JPreparedStatement}

trait JdbcConnection
  extends base.Connection {
  self: base.AbstractQuery with JdbcRow with JdbcMutableRow with JdbcParameterValue with base.Select with base.Update with base.SelectForUpdate =>

  override type UnderlyingConnection = JConnection

  override type PreparedStatement = JPreparedStatement

  implicit class JdbcConnection(underlying: JConnection) {

    def iteratorForUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Iterator[JdbcMutableRow] = {
      SelectForUpdate(queryText).on(
        parameterValues: _*
      ).iterator()(underlying)
    }

  }

}
