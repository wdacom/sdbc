package com.wda.sdbc.jdbc

import com.wda.sdbc.base
import java.sql.ResultSet

trait IsJdbcMutableRow extends IsJdbcRow with base.MutableRow[ResultSet] {

  //This flag is set to check and see if .updateRow() should be called.
  //It will be set to false when the iterator advances.
  private[sdbc] var wasUpdated: Boolean = false


  override def update(row: ResultSet, columnIndex: Int): Unit = {

  }

  def update[T, U](columnIndex: Int, value: T)(implicit TToParameterValue: T => JdbcParameterValue[U]): Unit = {
    wasUpdated = true
    value.update(this, columnIndex)
  }

  def update[T, U](columnName: String, value: T)(implicit TToParameterValue: T => JdbcParameterValue[U]): Unit = {
    wasUpdated = true
    value.update(this, columnName)
  }

}
