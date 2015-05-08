package com.wda.sdbc.base

trait GetterImplicits[WrappedRow, PreparedStatement] {
  self: Getter[WrappedRow, PreparedStatement] with Row[WrappedRow, PreparedStatement] =>

  implicit def GetterToRowSingleton[T](implicit getter: Getter[T]): Function[Row, T] = {
      row => getter(row).get
  }

  implicit def GetterToRowNullable[T](implicit getter: Getter[T]): Function[Row, Option[T]] = {
    row => getter(row)
  }

}
