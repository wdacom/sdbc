package com.wda.sdbc.base

import java.sql.{Array => _, _}

trait Getter[Row, Index, +T] {

  def indexOne: Index

  def apply(row: Row, columnIndex: Index): Option[T]

  def apply(row: Row): Option[T] = {
    apply(row, indexOne)
  }

}
