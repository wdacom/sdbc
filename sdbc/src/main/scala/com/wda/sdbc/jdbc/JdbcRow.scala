package com.wda.sdbc.jdbc

import java.sql.ResultSet

import com.wda.CaseInsensitiveOrdering
import scala.collection.immutable.TreeMap
import com.wda.sdbc.base

trait IsJdbcRow extends base.Row[ResultSet] {

  override def columnIndex(row: ResultSet, columnName: String): Option[Int] = {
    0.until(row.getMetaData.getColumnCount).find { index =>
      val columnNameAtIndex = row.getMetaData.getColumnName(index)
      columnName == columnNameAtIndex
    }
  }

}

trait JdbcRowImplicits {

  implicit class ResultSetToRowIterator(rs: java.sql.ResultSet) {

    def dbms = DBMS.of(rs)

    /**
     * Get an iterator over the result set.
     * It closes itself when you reach the end, or when you call close().
     * Only one iterator is ever created for a result set.
     * If you want another iterator, execute the select statement again.
     * @return
     */
    def iterator(): Iterator[ResultSet] = {

      new Iterator[ResultSet] {

        override def hasNext: Boolean = {
          val result = rs.next()
          if (!result) {
            util.Try(rs.close())
          }
          result
        }

        override def next(): ResultSet = {
          rs
        }

      }

    }
  }

}
