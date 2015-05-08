package com.wda.sdbc.base

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.time._
import java.util.UUID

trait Getter {
  self: Row =>

  trait Getter[+T] extends Function[Row, Option[T]] {

    override def apply(row: Row): Option[T] = {
      apply(row, 1)
    }

    def apply(row: Row, columnName: String): Option[T] = {
      val columnIndex = row.columnIndexes(columnName)
      apply(row, columnIndex)
    }

    def apply(row: Row, columnIndex: Int): Option[T]

  }



}
