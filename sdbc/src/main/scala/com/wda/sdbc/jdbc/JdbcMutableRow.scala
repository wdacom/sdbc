package com.wda.sdbc.jdbc

import java.sql.ResultSet

class JdbcMutableRow private[sdbc](
  underlying: ResultSet,
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

trait JdbcMutableRowImplicits {

  implicit class ResultSetToJdbcMutableRow(rs: ResultSet) {
    def mutableIterator(): Iterator[JdbcMutableRow] = {

      def dbms = DBMS.of(rs)

      val mutableRow = new JdbcMutableRow(rs, dbms)

      new Iterator[JdbcMutableRow] {

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

        override def next(): JdbcMutableRow = {
          mutableRow
        }

      }
    }
  }
}