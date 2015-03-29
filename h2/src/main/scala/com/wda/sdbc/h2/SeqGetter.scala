package com.wda.sdbc.h2

import java.io

import com.wda.sdbc.base.{Getter, Row}

trait SeqGetter {
  self: Row with Getter =>

  implicit val SeqGetter: Getter[Seq[io.Serializable]] =
    new Getter[Seq[io.Serializable]] {
      override def apply(row: Row, columnIndex: Int): Option[Seq[io.Serializable]] = {
        Option(row.getObject(columnIndex)).map(_.asInstanceOf[Array[io.Serializable]].toSeq)
      }
    }

}
