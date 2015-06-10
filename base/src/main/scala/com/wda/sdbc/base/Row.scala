package com.wda.sdbc.base

/**
 * Rows provide a uniform interfaces for getting a value from a row
 * composed of primitive parts, which are accessed by index. The
 * primitives are defined by the implicit `Getter`s that are in scope.
 */
object Row {
  def apply[Row, Index, T](row: Row)(implicit getter: Getter[Row, Index, T]): Function[Index, Option[T]] = {
    getter(row)
  }
}
