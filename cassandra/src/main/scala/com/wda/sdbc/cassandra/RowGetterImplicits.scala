package com.wda.sdbc.cassandra

import com.datastax.driver.core.{Row => CRow}

trait RowGetterImplicits {
    self: Row with IndexImplicits =>

  implicit def GetterToRowConverterOption[T](implicit getter: RowGetter[T]): CRow => Option[T] = { row =>
    getter(row, 0)
  }

  implicit def GetterToRowConverter[T](implicit getter: RowGetter[T]): CRow => T = { row =>
    getter(row, 0).get
  }

}
