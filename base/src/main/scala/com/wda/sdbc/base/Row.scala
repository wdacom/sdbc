package com.wda.sdbc.base

trait Row[UnderlyingRow, Index] {

  protected def underlying: UnderlyingRow
  
  def apply[V]()(implicit getter: Getter[UnderlyingRow, Index, V]): V = {
    single[V]()
  }

  def apply[V]( columnIndex: Index)(implicit getter: Getter[UnderlyingRow, Index, V]): V = {
    single[V](columnIndex)
  }

  def single[V]()(implicit getter: Getter[UnderlyingRow, Index, V]): V = {
    getter(underlying).get
  }

  def single[V]( columnIndex: Index)(implicit getter: Getter[UnderlyingRow, Index, V]): V = {
    getter(underlying, columnIndex).get
  }

  def option[V]()(implicit getter: Getter[UnderlyingRow, Index, V]): Option[V] = {
    getter(underlying)
  }

  def option[V]( columnIndex: Index)(implicit getter: Getter[UnderlyingRow, Index, V]): Option[V] = {
    getter(underlying, columnIndex)
  }

  def unapply[V](converter: PartialFunction[Row[UnderlyingRow, Index], V]): Option[V] = {
    if (converter.isDefinedAt(this)) Some(converter(this))
    else None
  }

  def unapplySeq[V](converters: PartialFunction[Row[UnderlyingRow, Index], V]*): Option[V] = {
    converters.find(_.isDefinedAt(this)).map(_(this))
  }

}
