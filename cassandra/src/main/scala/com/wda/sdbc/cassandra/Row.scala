package com.wda.sdbc.cassandra

import com.datastax.driver.core.{Row => CRow}

trait Row {

  implicit class Row(underlying: CRow) {

    def apply[T](ix: Index)(implicit getter: Getter[T]): Option[T] = {
      getter(underlying, ix)
    }

  }

}
