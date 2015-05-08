package com.wda.sdbc.jdbc

import com.wda.CaseInsensitiveOrdering
import com.wda.sdbc.base

import scala.collection.immutable.TreeMap

trait JdbcRow extends base.Row[java.sql.ResultSet] {
  self: JdbcParameterValue with JdbcGetter =>

  class JdbcRow private[sdbc](
    underlying: java.sql.ResultSet,
    val dbms: DBMS
  ) extends Row(underlying) {

    override val columnIndexes: Map[String, Int] = {
      //This works because "1 to 0" gives an empty list.
      TreeMap[String, Int](
        1.to(underlying.getMetaData.getColumnCount).map { case i => underlying.getMetaData.getColumnName(i) -> i}: _*
      )(CaseInsensitiveOrdering)
    }

  }

  class MutableJdbcRow private[sdbc](
    underlying: java.sql.ResultSet,
    dbms: DBMS
  ) extends JdbcRow(underlying, dbms) {

    //This flag is set to check and see if .updateRow() should be called.
    //It will be set to false when the iterator advances.
    private[sdbc] var wasUpdated: Boolean = false

    def update[T, U](columnIndex: Int, value: T)(implicit TToParameterValue: T => JdbcParameterValue[U]): Unit = {
      wasUpdated = true
      value.update(this, columnIndex)
    }

    def update[T, U](columnName: String, value: T)(implicit TToParameterValue: T => JdbcParameterValue[U]): Unit = {
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
    def iterator(): Iterator[JdbcRow] = {

      val row = new JdbcRow(rs, dbms)

      new Iterator[JdbcRow] {

        override def hasNext: Boolean = {
          val result = rs.next()
          if (!result) {
            util.Try(rs.close())
          }
          result
        }

        override def next(): JdbcRow = {
          row
        }

      }

    }

    def mutableIterator(): Iterator[MutableJdbcRow] = {

      val mutableRow = new MutableJdbcRow(rs, dbms)

      new Iterator[MutableJdbcRow] {

        override def hasNext: Boolean = {
          if (mutableRow.wasUpdated) {
            rs.updateRow()
            mutableRow.wasUpdated = false
          }

          val result = rs.next()
          if (!result) {
            util.Try(rs.close())
          }
          result
        }

        override def next(): MutableJdbcRow = {
          mutableRow
        }

      }
    }
  }

}
