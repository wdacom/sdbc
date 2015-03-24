package com.wda.sdbc.base

trait GetterImplicits {
  self: Getter with Row =>

  implicit def GetterToRowSingleton[T](implicit getter: Getter[T]): Function[Row, T] = {
      row => getter(row).get
  }

  implicit def GetterToRowNullable[T](implicit getter: Getter[T]): Function[Row, Option[T]] = {
    row => getter(row)
  }

}
