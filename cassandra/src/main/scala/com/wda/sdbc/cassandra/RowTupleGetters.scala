package com.wda.sdbc.cassandra

import com.datastax.driver.core.{Row => CRow}

trait RowTupleGetters {

  implicit val Tuple0Getter: RowGetter[Unit] = new RowGetter[Unit] {
    override def apply(
      row: CRow,
      ix: Index
    ): Option[Unit] = {
      val columnIndex = ix(row)
      if (row.isNull(columnIndex)) None
      else Some(())
    }
  }

  implicit def Tuple1Getter[T0](implicit getter0: TupleGetter[T0]): RowGetter[(Option[T0])] = new RowGetter[(Option[T0])] {
    override def apply(
      row: CRow,
      ix: Index
    ): Option[(Option[T0])] = {
      val columnIndex = ix(row)
      Option(row.getTupleValue(columnIndex)).map { case tupleValue =>
        getter0(tupleValue, 0)
      }
    }
  }

  implicit def Tuple2Getter[T0, T1](implicit
    getter0: TupleGetter[T0],
    getter1: TupleGetter[T1]
  ): RowGetter[(Option[T0], Option[T1])] = new RowGetter[(Option[T0], Option[T1])] {
    override def apply(
      row: CRow,
      ix: Index
    ): Option[(Option[T0], Option[T1])] = {
      val columnIndex = ix(row)
      Option(row.getTupleValue(columnIndex)).map { case tupleValue =>
        (getter0(tupleValue, 0), getter1(tupleValue, 1))
      }
    }
  }

}
