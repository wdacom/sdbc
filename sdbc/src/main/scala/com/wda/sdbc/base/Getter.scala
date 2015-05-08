package com.wda.sdbc.base

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.time._
import java.util.UUID

trait Getter {
  self: Row =>

  trait Getter[+T] extends Function[UnderlyingRow, Option[T]] {

    override def apply(row: UnderlyingRow): Option[T] = {
      implicit val getter = this
      isRow.option(row, 1)
    }

    def apply(row: UnderlyingRow, columnName: String): Option[T] = {
      val columnIndex = isRow.columnIndex(row, columnName)
      implicit val getter = this
      isRow.option(row, columnIndex)
    }

    def apply(row: UnderlyingRow, columnIndex: Int): Option[T]

  }

}
