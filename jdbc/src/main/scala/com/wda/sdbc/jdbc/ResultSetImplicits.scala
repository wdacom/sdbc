package com.wda.sdbc.jdbc

import java.io.Closeable
import java.sql.ResultSet

trait ResultSetImplicits {

  implicit class ResultSetToRowIterator(underlying: ResultSet) {

    /**
     * Get an iterator over the result set.
     * It closes itself when you reach the end, or when you call close().
     * Only one iterator is ever created for a result set.
     * If you want another iterator, execute the select statement again.
     * @return
     */
    def iterator(): Iterator[Row] with Closeable = {

      new Iterator[Row] with Closeable {

        val row = new Row(underlying)

        override def close(): Unit = {
          underlying.close()
        }

        override def hasNext: Boolean = {
          val result = underlying.next()
          if (!result) {
            close()
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
    def mutableIterator(): Iterator[MutableRow] with Closeable = {

      if ((underlying.getConcurrency & ResultSet.CONCUR_UPDATABLE) == 0) {
        throw new IllegalStateException("The ResultSet's concurrency is not CONCUR_UPDATABLE.")
      }

      new Iterator[MutableRow] with Closeable {

        val row = new MutableRow(underlying)

        override def close(): Unit = {
          underlying.close()
        }

        override def hasNext: Boolean = {
          val result = underlying.next()
          if (!result) {
            close()
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