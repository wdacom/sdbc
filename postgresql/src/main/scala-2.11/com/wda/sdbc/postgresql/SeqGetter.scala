package com.wda.sdbc.postgresql

import scala.collection.immutable.Seq
import com.wda.sdbc.jdbc._

trait SeqGetter {
  self: IndexImplicits with ResultSetImplicits =>

  implicit def SeqGetterOption[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] =
    new Getter[Seq[Option[T]]] {
      override def apply(row: Row): (Index) => Option[Seq[Option[T]]] = { ix =>
        Option(row.getArray(ix(row))).map(_.getResultSet().iterator().map(_[T](2)).toVector)
      }
    }

  implicit def SeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] =
    new Getter[Seq[T]] {
      override def apply(row: Row): (Index) => Option[Seq[T]] = { ix =>
        SeqGetterOption(getter)(row)(ix).map(_.map(_.get))
      }
    }
  
  implicit class RowSeqOps(row: Row) {

    def seq[T](ix: Index)(implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      Option(row.getArray(ix(row))).map { array =>
        val arrayAsResultSet = array.getResultSet
        arrayAsResultSet.iterator().map { row =>
          row[T](2)(getter)
        }.toVector
      }
    }

  }

}
