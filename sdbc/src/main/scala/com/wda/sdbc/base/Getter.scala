package com.wda.sdbc.base

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.time._
import java.util.UUID

import scala.annotation.unspecialized

trait Getter[UnderlyingRow, +T] {

  def apply(row: UnderlyingRow)(implicit isRow: Row[UnderlyingRow]): Option[T] = {
    apply(row, 1)
  }

  def apply(row: UnderlyingRow, columnName: String)(implicit isRow: Row[UnderlyingRow]): Option[T] = {
    val columnIndex = isRow.columnIndex(row, columnName)
    implicit val getter = this
    isRow.option(row, columnIndex)
  }

  def apply(row: UnderlyingRow, columnIndex: Int): Option[T]

}
