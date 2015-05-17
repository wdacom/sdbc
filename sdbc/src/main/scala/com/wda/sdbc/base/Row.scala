package com.wda.sdbc.base

trait Row[UnderlyingRow] {

  def findColumnIndex(row: UnderlyingRow, columnName: String): Option[Int]

  def apply[V](row: UnderlyingRow)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): V = {
    single[V](row)
  }

  def apply[V](row: UnderlyingRow, columnName: String)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): V = {
    single[V](row, columnName)
  }

  def apply[V](row: UnderlyingRow, columnIndex: Int)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): V = {
    single[V](row, columnIndex)
  }

  def single[V](row: UnderlyingRow)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): V = {
    getter(row).get
  }

  def single[V](row: UnderlyingRow, columnName: String)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): V = {
    getter(row, columnName).get
  }

  def single[V](row: UnderlyingRow, columnIndex: Int)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): V = {
    getter(row, columnIndex).get
  }

  def option[V](row: UnderlyingRow)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): Option[V] = {
    getter(row)
  }

  def option[V](row: UnderlyingRow)(columnName: String)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): Option[V] = {
    getter(row, columnName)
  }

  def option[V](row: UnderlyingRow, columnIndex: Int)(implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, V]): Option[V] = {
    getter(row, columnIndex)
  }

  def unapply[V](converter: PartialFunction[Row[UnderlyingRow], V]): Option[V] = {
    if (converter.isDefinedAt(this)) Some(converter(this))
    else None
  }

  def unapplySeq[V](converters: PartialFunction[Row[UnderlyingRow], V]*): Option[V] = {
    converters.find(_.isDefinedAt(this)).map(_(this))
  }

}
