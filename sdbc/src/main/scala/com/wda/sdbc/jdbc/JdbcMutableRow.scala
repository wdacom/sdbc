package com.wda.sdbc.jdbc

import java.sql.ResultSet

class JdbcMutableRow private[sdbc](
  underlying: ResultSet,
  dbms: DBMS
) extends JdbcRow(underlying, dbms) {

  //This flag is set to check and see if .updateRow() should be called.
  //It will be set to false when the iterator advances.
  private[sdbc] var wasUpdated: Boolean = false

  def update[T, U](columnIndex: Int, value: T)(implicit TToParameterValue: T => JdbcParameterValue[U]): Unit = {
    wasUpdated = true
    value.update(this, columnIndex)
  }

  def update[T, U](columnName: String, value: T)(implicit TToParameterValue: T => JdbcParameterValue[U]): Unit = {
    wasUpdated = true
    value.update(this, columnName)
  }

}
