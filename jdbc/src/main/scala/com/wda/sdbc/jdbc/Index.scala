package com.wda.sdbc.jdbc

import java.sql.SQLException

case class IntIndex(columnIndex: Int) extends Index {
  override def isDefinedAt(row: Row): Boolean = {
    columnIndex < row.getMetaData.getColumnCount
  }

  override def apply(row: Row): Int = columnIndex + 1
}

case class StringIndex(columnLabel: String) extends Index {
  override def isDefinedAt(row: Row): Boolean = {
    try {
      apply(row) >= 0
    } catch {
      case e: SQLException =>
        false
    }
  }

  override def apply(row: Row): Int = {
    row.findColumn(columnLabel)
  }
}
