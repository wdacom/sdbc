package com.wda.sdbc.base

import scala.collection.immutable.Seq

trait QueryMethods[UnderlyingConnection, Execute, Select, Update, UnderlyingResultSet, UnderlyingRow] extends Closable[UnderlyingConnection] {

  def execute(
    connection: UnderlyingConnection,
    underlyingQuery: Execute
  ): Unit

  def iterator[T](
    connection: UnderlyingConnection,
    underlyingQuery: Select
  )(implicit converter: Row[UnderlyingRow] => T
  ): Iterator[T]

  def seq[T](
    connection: UnderlyingConnection,
    underlyingQuery: Select
  )(implicit converter: Row[UnderlyingRow] => T
  ): Seq[T] = {
    iterator[T](connection, underlyingQuery).toVector
  }

  def option[T](
    connection: UnderlyingConnection,
    underlyingQuery: Select
  )(implicit converter: Row[UnderlyingRow] => T
  ): Option[T] = {
    iterator[T](connection, underlyingQuery).toStream.headOption
  }

  def single[T](
    connection: UnderlyingConnection,
    underlyingQuery: Select
  )(implicit converter: Row[UnderlyingRow] => T
  ): T = {
    option[T](
      connection,
      underlyingQuery
    ).get
  }

  def update[T](
    connection: UnderlyingConnection,
    underlyingQuery: Update
  ): Long

}
