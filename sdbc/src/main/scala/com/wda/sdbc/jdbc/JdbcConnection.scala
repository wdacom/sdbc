package com.wda.sdbc.jdbc

import com.wda.sdbc.base
import java.sql.{Connection => JConnection, ResultSet, PreparedStatement}

trait JdbcConnection
  extends base.Connection[ResultSet, JConnection, PreparedStatement, ResultSet] {
  self: DBMS with JdbcRow =>

  implicit class JdbcConnection(underlying: JConnection)
    extends Connection(underlying) {

    if (DBMS.of(underlying).getClass != self.getClass) {
      throw new IllegalArgumentException("Connection is for the wrong DBMS.")
    }

    implicit val dbms: DBMS = self

    def iteratorForUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
      ): Iterator[MutableJdbcRow] = {
      SelectForUpdate(queryText).on(
        parameterValues: _*
      ).iterator()(this)
    }

  }

}
