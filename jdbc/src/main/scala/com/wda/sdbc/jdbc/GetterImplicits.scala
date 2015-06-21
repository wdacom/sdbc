package com.wda.sdbc.jdbc

trait GetterImplicits {
  self: IndexImplicits =>

  implicit def GetterToRowConverterOption[T](implicit getter: Getter[T]): Row => Option[T] = { row =>
    getter(row, 0)
  }

  implicit def GetterToRowConverter[T](implicit getter: Getter[T]): Row => T = { row =>
    getter(row, 0).get
  }

}
