package com.wda.sdbc.base

trait MutableRow[UnderlyingMutableRow] extends Row[UnderlyingMutableRow] {

  def update(row: UnderlyingMutableRow, columnName: String): Unit = {
    update(row, columnIndex(row, columnName))
  }

  def update(row: UnderlyingMutableRow, columnIndex: Int): Unit
}
