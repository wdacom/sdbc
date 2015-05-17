package com.wda.sdbc.base

import java.util.NoSuchElementException

trait MutableRow[UnderlyingMutableRow] {
  self: Row[UnderlyingMutableRow] =>

  def update(row: UnderlyingMutableRow, columnName: String): Unit = {
    findColumnIndex(row, columnName) match {
      case None =>
        throw new NoSuchElementException(columnName)
      case Some(columnIndex) =>
        update(row, columnIndex)
    }
  }

  def update(row: UnderlyingMutableRow, columnIndex: Int): Unit
}
