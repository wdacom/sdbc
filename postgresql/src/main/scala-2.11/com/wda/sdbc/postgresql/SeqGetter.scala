package com.wda.sdbc.postgresql

import scala.collection.immutable.Seq
import com.wda.sdbc.jdbc._

trait SeqGetter {
  self: IndexImplicits with ResultSetImplicits =>

  implicit def SeqGetterOption[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = {
    (row: Row) => (ix: Index) =>
      Option(row.getArray(ix(row))).map(_.getResultSet().iterator().map(_[T](2)).toVector)
  }

  implicit def SeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = {
    (row: Row) => (ix: Index) =>
      SeqGetterOption(getter)(row)(ix).map(_.map(_.get))
  }

}
