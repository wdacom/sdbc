package com.wda.sdbc.postgresql.jdbc.implementation

import com.wda.sdbc.base.jdbc._

trait SeqGetterImplicits {

  implicit def GetterToSeqOptionGetter[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = {
    (row: Row, ix: Index) =>
      Option(row.getArray(ix(row))).map(_.getResultSet().iterator().map(_[T](IntIndex(1))).toVector)
  }

  implicit def GetterToSeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = {
    (row: Row, ix: Index) =>
      GetterToSeqOptionGetter(getter)(row, ix).map(_.map(_.get))
  }

}
