package com.wda.sdbc.jdbc

import com.wda.sdbc.base
import java.sql.{Connection => JConnection, PreparedStatement => JPreparedStatement, ResultSet}

trait JdbcConnection
  extends base.Connection[JConnection, JPreparedStatement, ResultSet, ResultSet] {
    self: base.Select[JConnection, JPreparedStatement, ResultSet, ResultSet] =>

  override def prepare(connection: JConnection, queryText: String): JPreparedStatement = {
    connection.prepareStatement(queryText)
  }

  override def execute(preparedStatement: JPreparedStatement): Unit = {
    preparedStatement.execute()
  }



}
