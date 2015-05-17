package com.wda.sdbc.base

trait GetterImplicits[UnderlyingRow] {

  implicit def GetterToRowSingleton[T](implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, T]): Function[UnderlyingRow, T] = {
      row => getter(row).get
  }

  implicit def GetterToRowNullable[T](implicit isRow: Row[UnderlyingRow], getter: Getter[UnderlyingRow, T]): Function[UnderlyingRow, Option[T]] = {
    row => getter(row)
  }

}
