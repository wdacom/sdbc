package com.wda.sdbc.base

trait Row {
  self: ParameterValue with Getter =>

  type UnderlyingRow

  val isRow: Row

  trait Row {

    def columnIndex(row: UnderlyingRow, columnName: String): Int

    def apply[V](row: UnderlyingRow)(implicit getter: Getter[V]): V = {
      single[V](row)
    }

    def apply[V](row: UnderlyingRow, columnName: String)(implicit getter: Getter[V]): V = {
      single[V](row, columnName)
    }

    def apply[V](row: UnderlyingRow, columnIndex: Int)(implicit getter: Getter[V]): V = {
      single[V](row, columnIndex)
    }

    def single[V](row: UnderlyingRow)(implicit getter: Getter[V]): V = {
      getter(row).get
    }

    def single[V](row: UnderlyingRow, columnName: String)(implicit getter: Getter[V]): V = {
      getter(row, columnName).get
    }

    def single[V](row: UnderlyingRow, columnIndex: Int)(implicit getter: Getter[V]): V = {
      getter(row, columnIndex).get
    }

    def option[V](row: UnderlyingRow)(implicit getter: Getter[V]): Option[V] = {
      getter(row)
    }

    def option[V](row: UnderlyingRow)(columnName: String)(implicit getter: Getter[V]): Option[V] = {
      getter(row, columnName)
    }

    def option[V](row: UnderlyingRow, columnIndex: Int)(implicit getter: Getter[V]): Option[V] = {
      getter(row, columnIndex)
    }

    def unapply[V](converter: PartialFunction[Row, V]): Option[V] = {
      if (converter.isDefinedAt(this)) Some(converter(this))
      else None
    }

    def unapplySeq[V](converters: PartialFunction[Row, V]*): Option[V] = {
      converters.find(_.isDefinedAt(this)).map(_(this))
    }

  }

}
