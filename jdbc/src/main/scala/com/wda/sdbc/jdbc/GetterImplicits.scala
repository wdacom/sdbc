package com.wda.sdbc.jdbc

trait GetterImplicits {
  self: IndexImplicits =>

  implicit def GetterToRowSingleton[T](implicit getter: Getter[T]): Function[Row, T] = {
    row => row[T](1).get
  }

  implicit def GetterToRowNullable[T](implicit getter: Getter[T]): Function[Row, Option[T]] = {
    row => row[T](1)
  }

}
