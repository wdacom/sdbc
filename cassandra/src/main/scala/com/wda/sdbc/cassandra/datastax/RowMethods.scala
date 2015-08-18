package com.wda.sdbc.cassandra.datastax

import com.datastax.driver.core.{Row => CRow}

trait RowMethods {

  implicit class Row(underlying: CRow) {

    def apply[T](ix: Index)(implicit getter: RowGetter[T]): Option[T] = {
      getter(underlying, ix)
    }

  }

}
