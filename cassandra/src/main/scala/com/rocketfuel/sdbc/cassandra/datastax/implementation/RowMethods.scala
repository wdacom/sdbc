package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.datastax.driver.core.{Row => CRow, TupleValue}

trait RowMethods {

  implicit class Row(underlying: CRow) {

    def get[T](ix: Index)(implicit getter: RowGetter[T]): Option[T] = {
      getter(underlying, ix)
    }

    def parameter(ix: Index): Option[ParameterValue[_]] = {
      val intIx = ix(underlying)
      val columns = underlying.getColumnDefinitions
      val columnType = columns.getType(intIx)
      val columnClass = columnType.asJavaClass()
      if (columnClass.equals(classOf[java.util.Map[_, _]])) {
        ???
      } else if (columnClass.equals(classOf[java.util.Set[_]])) {
        ???
      } else if (columnClass.equals(classOf[java.util.List[_]])) {
        ???
      } else if (columnClass.equals(classOf[TupleValue])) {
        ???
      } else {
        ???
      }
    }

  }

}
