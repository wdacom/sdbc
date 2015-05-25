package com.wda.sdbc.jdbc

import java.sql.{ResultSet, Connection}
import com.wda.sdbc.base
import scala.collection.immutable.Seq

object Select {

  def iterator[T]()(implicit select: JdbcSelect[T], connection: Connection): Iterator[T] = {
    select.iterator()
  }

  def seq[T](implicit select: JdbcSelect[T], connection: Connection): Seq[T] = {
    select.iterator().toVector
  }

  def option[T]()(implicit select: JdbcSelect[T], connection: Connection): Option[T] = {
    select.iterator().toStream.headOption
  }

  def single[T]()(implicit select: JdbcSelect[T], connection: Connection): T = {
    select.iterator().toStream.head
  }

}
