package com.wda.sdbc.h2

import java.io

import com.wda.sdbc.base.{Getter, Row}

trait SerializableGetter {
  self: Getter with Row =>

  implicit val SerializableGetter = new Getter[io.Serializable] {
    override def apply(row: Row, columnIndex: Int): Option[io.Serializable] = {
      Option(row.getObject(columnIndex)).map(_.asInstanceOf[io.Serializable])
    }
  }

}
