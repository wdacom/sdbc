package com.wda.sdbc.base

trait GetterImplicits[Row, Index] {

  implicit def GetterToRowSingleton[T](implicit isRow: Row, getter: Getter[Row, Index, T]): Function[Row, T] = {
      row => getter(row).get
  }

  implicit def GetterToRowNullable[T](implicit isRow: Row, getter: Getter[Row, Index, T]): Function[Row, Option[T]] = {
    row => getter(row)
  }

}
