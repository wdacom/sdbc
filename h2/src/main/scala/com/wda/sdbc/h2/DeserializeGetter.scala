package com.wda.sdbc.h2

import com.wda.sdbc.base.{Getter, Row}

import scala.reflect.ClassTag

trait DeserializeGetter {
  self: Getter with Row with SerializeParameterValue =>

  case class Deserialize[T](value: T)

  implicit def DeserializeGetter[T](implicit ctag: ClassTag[T]): Getter[Deserialize[T]] = {
    new Getter[Deserialize[T]] {
      override def apply(row: Row, columnIndex: Int): Option[Deserialize[T]] = {
        Option(row.getObject(columnIndex)).map(o => Deserialize[T](o.asInstanceOf[T]))
      }
    }
  }

}
