package com.wda.sdbc.jdbc

import java.sql.ResultSet

trait ResultSetImplicits {

  implicit class ResultSetToRowIterator(underlying: ResultSet) {

    def dbms = DBMS.of(underlying)

    /**
     * Get an iterator over the result set.
     * It closes itself when you reach the end, or when you call close().
     * Only one iterator is ever created for a result set.
     * If you want another iterator, execute the select statement again.
     * @return
     */
    def iterator(): Iterator[Row] = {

      new Iterator[Row] {

        val row = new Row(underlying)
        
        override def hasNext: Boolean = {
          val result = underlying.next()
          if (!result) {
            underlying.close()
          }
          result
        }

        override def next(): Row = {
          row
        }

      }

    }

    /**
     * Get an iterator over the mutable result set.
     * It closes itself when you reach the end, or when you call close().
     * Only one iterator is ever created for a result set.
     * If you want another iterator, execute the select statement again.
     * @return
     */
    def mutableIterator(): Iterator[MutableRow] = {

      new Iterator[MutableRow] {

        val row = new MutableRow(underlying)

        override def hasNext: Boolean = {
          val result = underlying.next()
          if (!result) {
            underlying.close()
          }
          result
        }

        override def next(): MutableRow = {
          row
        }

      }

    }

  }

}