package com.wda.sdbc.base

abstract class Select[Connection] {

  def iterator[T]()(implicit selectable: Selectable[T, Connection], connection: Connection): Iterator[T] = {
    selectable.iterator()
  }

  def seq[T]()(implicit selectable: Selectable[T, Connection], connection: Connection): Seq[T] = {
    iterator[T]().toVector
  }

  def option[T]()(implicit selectable: Selectable[T, Connection], connection: Connection): Option[T] = {
    iterator[T]().toStream.headOption
  }

  def single[T]()(implicit selectable: Selectable[T, Connection], connection: Connection): T = {
    option[T]().get
  }

}
