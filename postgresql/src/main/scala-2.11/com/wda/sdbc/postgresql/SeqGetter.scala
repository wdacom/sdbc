package com.wda.sdbc.postgresql

import com.wda.sdbc.base.Getter
import com.wda.sdbc.base.Row

import scala.collection.immutable.Seq

trait SeqGetter {
  self: Row with Getter =>

  implicit def GetterToSeqOptionGetter[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = new Getter[Seq[Option[T]]] {
    override def apply(row: Row, columnIndex: Int): Option[Seq[Option[T]]] = {
      row.optionSeq[T](columnIndex)
    }
  }

  implicit def GetterToSeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = new Getter[Seq[T]] {
    override def apply(row: Row, columnIndex: Int): Option[Seq[T]] = {
      row.optionSeq[T](columnIndex).map(_.map(_.get))
    }
  }

  implicit class RowSeqOps(row: Row) {

    def seq[T]()(implicit getter: Getter[T]): Seq[Option[T]] = {
      optionSeq[T].get
    }

    def seq[T](columnName: String)(implicit getter: Getter[T]): Seq[Option[T]] = {
      optionSeq[T](columnName).get
    }

    def seq[T](columnIndex: Int)(implicit getter: Getter[T]): Seq[Option[T]] = {
      optionSeq[T](columnIndex).get
    }

    def optionSeq[T](implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      optionSeq(1)
    }

    def optionSeq[T](columnName: String)(implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      optionSeq(row.columnIndexes(columnName))
    }

    def optionSeq[T](columnIndex: Int)(implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      Option(row.getArray(columnIndex)).map { array =>
        val arrayAsResultSet = array.getResultSet
        arrayAsResultSet.iterator().map { row =>
          row.option[T](2)(getter)
        }.toVector
      }
    }

  }

}
