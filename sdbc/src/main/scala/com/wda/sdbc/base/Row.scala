package com.wda.sdbc.base

trait Row[WrappedRow, PreparedStatement] {
  self: ParameterValue[WrappedRow, PreparedStatement] with Getter[WrappedRow, PreparedStatement] =>

  abstract class Row private[sdbc](
    val underlying: WrappedRow
  ) {

    def columnIndexes: Map[String, Int]

    type MyGetter[+T] = Getter[T]

    def apply[V](implicit getter: MyGetter[V]): V = {
      single[V]
    }

    def apply[V](columnName: String)(implicit getter: MyGetter[V]): V = {
      single[V](columnName)
    }

    def apply[V](columnIndex: Int)(implicit getter: MyGetter[V]): V = {
      single[V](columnIndex)
    }

    def single[V](implicit getter: MyGetter[V]): V = {
      getter(this).get
    }

    def single[V](columnName: String)(implicit getter: MyGetter[V]): V = {
      getter(this, columnName).get
    }

    def single[V](columnIndex: Int)(implicit getter: MyGetter[V]): V = {
      getter(this, columnIndex).get
    }

    def option[V](implicit getter: MyGetter[V]): Option[V] = {
      getter(this)
    }

    def option[V](columnName: String)(implicit getter: MyGetter[V]): Option[V] = {
      getter(this, columnName)
    }

    def option[V](columnIndex: Int)(implicit getter: MyGetter[V]): Option[V] = {
      getter(this, columnIndex)
    }

    def unapply[V](converter: PartialFunction[Row, V]): Option[V] = {
      if (converter.isDefinedAt(this)) Some(converter(this))
      else None
    }

    def unapplySeq[V](converters: PartialFunction[Row, V]*): Option[V] = {
      converters.find(_.isDefinedAt(this)).map(_(this))
    }

  }

  implicit def RowToWrappedRow(row: Row): WrappedRow = {
    row.underlying
  }

}
