package com.wda.sdbc.base

trait MutableRow {
  self: Row with ParameterValue with Getter =>

  type UnderlyingMutableRow <: UnderlyingRow

  trait MutableRow extends Row {
    def update(row: UnderlyingMutableRow, columnName: String): Unit = {
      update(row, columnIndex(row, columnName))
    }

    def update(row: UnderlyingMutableRow, columnIndex: Int): Unit
  }

}
