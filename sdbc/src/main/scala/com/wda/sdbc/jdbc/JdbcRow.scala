package com.wda.sdbc.jdbc

import java.sql.ResultSet

import com.wda.CaseInsensitiveOrdering
import scala.collection.immutable.TreeMap
import com.wda.sdbc.base

class JdbcRow private[sdbc](
  val underlying: ResultSet,
  val dbms: DBMS
) {

  val columnIndexes: Map[String, Int] = {
    //This works because "1 to 0" gives an empty list.
    TreeMap[String, Int](
      1.to(underlying.getMetaData.getColumnCount).map { case i => underlying.getMetaData.getColumnName(i) -> i}: _*
    )(CaseInsensitiveOrdering)
  }

}

trait IsJdbcRow extends base.Row[JdbcRow] {
  self: base.Getter[JdbcRow] =>
  override def columnIndex(row: JdbcRow, columnName: String): Int = {
    row.columnIndexes(columnName)
  }
}

trait JdbcRowImplicits {

  implicit def JdbcRowToUnderlyingRow(row: JdbcRow): ResultSet = {
    row.underlying
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
  }

}
