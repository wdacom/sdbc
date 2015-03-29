package com.wda.sdbc.h2

import com.wda.sdbc.base.{Getter, Row}

trait SeqGetter {
  self: Row with Getter =>

  implicit def SeqGetter[T]: Getter[Seq[T]] =
    new Getter[Seq[T]] {
      override def apply(row: Row, columnIndex: Int): Option[Seq[T]] = {
        Option(row.getArray(columnIndex)).
        map { array =>
          try {
            array.getArray().asInstanceOf[Array[T]].toSeq
          } finally {
            array.free()
          }
        }
      }
    }

}
