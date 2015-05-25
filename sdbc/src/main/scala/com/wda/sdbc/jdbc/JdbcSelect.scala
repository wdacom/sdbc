package com.wda.sdbc.jdbc

import com.wda.Logging
import com.wda.sdbc.base
import java.sql._

import com.wda.sdbc.base.{Row, ParameterValue, CompiledStatement}

trait JdbcSelect[T]
  extends base.Select[T, Connection, ResultSet]
  with base.ParameterizedQuery[JdbcSelect, ResultSet]
  with Logging {
  self: Logging with JdbcRowImplicits =>

  def prepare()(implicit connection: Connection): PreparedStatement = {
    connection.prepareStatement(queryText)
  }

  override def iterator()(implicit connection: Connection): Iterator[T] = {

  }

  def seq()(implicit connection: Connection): Iterator[T] = {
    val preparedStatement = prepare()
    val results = preparedStatement.executeQuery()
    try {
      results.iterator().map[T](converter).toVector
    } finally {
      util.Try(preparedStatement.close())
    }
  }

}
