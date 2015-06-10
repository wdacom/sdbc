package com.wda.sdbc.jdbc

trait IndexImplicits {

  implicit def IntToRowIndex(columnIndex: Int): Index =
    new Index {
      override def isDefinedAt(row: Row): Boolean = {
        columnIndex < row.getMetaData.getColumnCount
      }

      override def apply(row: Row): Int = columnIndex
    }

  implicit def StringToRowIndex(columnLabel: String): Index =
    new Index {
      override def isDefinedAt(row: Row): Boolean = {
        util.Try(apply(row)).isSuccess
      }

      override def apply(row: Row): Int = {
        row.findColumn(columnLabel)
      }
    }

}
