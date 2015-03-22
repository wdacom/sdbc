package com.wda.sdbc.base

import com.wda.CaseInsensitiveOrdering
import com.wda.sdbc.DBMS

import scala.collection.immutable.TreeMap

trait Row {
  self: ParameterValue with Getter =>

  class Row private[sdbc](
    val underlying: java.sql.ResultSet,
    val dbms: DBMS
  ) {

    val columnIndexes: Map[String, Int] = {
      //This works because "1 to 0" gives an empty list.
      TreeMap[String, Int](
        1.to(underlying.getMetaData.getColumnCount).map { case i => underlying.getMetaData.getColumnName(i) -> i}: _*
      )(CaseInsensitiveOrdering)
    }

    def apply[V](implicit getter: Getter[V]): V = {
      single[V]
    }

    def apply[V](columnName: String)(implicit getter: Getter[V]): V = {
      single[V](columnName)
    }

    def apply[V](columnIndex: Int)(implicit getter: Getter[V]): V = {
      single[V](columnIndex)
    }

    def single[V](implicit getter: Getter[V]): V = {
      getter(this).get
    }

    def single[V](columnName: String)(implicit getter: Getter[V]): V = {
      getter(this, columnName).get
    }

    def single[V](columnIndex: Int)(implicit getter: Getter[V]): V = {
      getter(this, columnIndex).get
    }

    def option[V](implicit getter: Getter[V]): Option[V] = {
      getter(this)
    }

    def option[V](columnName: String)(implicit getter: Getter[V]): Option[V] = {
      getter(this, columnName)
    }

    def option[V](columnIndex: Int)(implicit getter: Getter[V]): Option[V] = {
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

  class MutableRow private[sdbc](
    underlying: java.sql.ResultSet,
    dbms: DBMS
  ) extends Row(underlying, dbms) {

    //This flag is set to check and see if .updateRow() should be called.
    //It will be set to false when the iterator advances.
    private[sdbc] var wasUpdated: Boolean = false

    def update[T, U](columnIndex: Int, value: T)(implicit VToParameterValue: T => ParameterValue[U]): Unit = {
      wasUpdated = true
      value.update(this, columnIndex)
    }

    def update[T, U](columnName: String, value: T)(implicit VToParameterValue: T => ParameterValue[U]): Unit = {
      wasUpdated = true
      value.update(this, columnName)
    }

  }

  implicit class ResultSetToRowIterator(rs: java.sql.ResultSet) {

    def dbms = DBMS.of(rs)

    /**
     * Get an iterator over the result set.
     * It closes itself when you reach the end, or when you call close().
     * Only one iterator is ever created for a result set.
     * If you want another iterator, execute the select statement again.
     * @return
     */
    def iterator(): Iterator[Row] = {

      val row = new Row(rs, dbms)

      new Iterator[Row] {

        override def hasNext: Boolean = {
          val result = row.next()
          if (!result) {
            util.Try(row.close())
          }
          result
        }

        override def next(): Row = {
          row
        }

      }

    }

    def mutableIterator(): Iterator[MutableRow] = {

      val mutableRow = new MutableRow(rs, dbms)

      new Iterator[MutableRow] {

        override def hasNext: Boolean = {
          if (mutableRow.wasUpdated) {
            rs.updateRow()
            mutableRow.wasUpdated = false
          }

          val result = mutableRow.next()
          if (!result) {
            util.Try(mutableRow.close())
          }
          result
        }

        override def next(): MutableRow = {
          mutableRow
        }

      }
    }
  }

  implicit def RowToResultSet(row: Row): java.sql.ResultSet = {
    row.underlying
  }

}
