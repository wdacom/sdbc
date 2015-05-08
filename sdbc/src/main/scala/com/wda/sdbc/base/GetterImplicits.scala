package com.wda.sdbc.base

trait GetterImplicits {
  self: Getter with Row =>

  implicit def GetterToRowSingleton[T](implicit getter: Getter[T]): Function[UnderlyingRow, T] = {
      row => getter(row).get
  }

  implicit def GetterToRowNullable[T](implicit getter: Getter[T]): Function[UnderlyingRow, Option[T]] = {
    row => getter(row)
  }

}
