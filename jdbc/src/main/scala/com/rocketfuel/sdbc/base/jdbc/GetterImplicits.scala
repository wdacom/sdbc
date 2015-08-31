package com.rocketfuel.sdbc.base.jdbc

trait GetterImplicits {
  self: IndexImplicits =>

  implicit def GetterToRowConverterOption[T](implicit getter: Getter[T]): MutableRow => Option[T] = { row =>
    getter(row, 0)
  }

  implicit def GetterToRowConverter[T](implicit getter: Getter[T]): MutableRow => T = { row =>
    getter(row, 0).get
  }

  implicit def ParameterGetterToImmutableRowConverter(implicit getter: Getter[ParameterValue]): MutableRow => ImmutableRow = { row =>
    row.asImmutable
  }

}
