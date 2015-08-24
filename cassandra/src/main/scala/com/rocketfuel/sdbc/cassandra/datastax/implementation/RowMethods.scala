package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.datastax.driver.core.{Row => CRow}

trait RowMethods {
  self: ParameterValues with IndexImplicits =>

  implicit class Row(underlying: CRow) {

    def get[T](ix: Index)(implicit getter: RowGetter[T]): Option[T] = {
      getter(underlying, ix)
    }

    def getParameters(implicit getter: RowGetter[ParameterValue[_]]): IndexedSeq[Option[ParameterValue[_]]] = {
      IndexedSeq.tabulate(underlying.getColumnDefinitions.size())(ix => get[ParameterValue[_]](ix))
    }

    def getParametersByName(implicit getter: RowGetter[ParameterValue[_]]): Map[String, Option[ParameterValue[_]]] = {
      getParameters.zipWithIndex.foldLeft(Map.empty[String, Option[ParameterValue[_]]]) {
        case (accum, (value, ix)) =>
          accum + (underlying.getColumnDefinitions.getName(ix) -> value)
      }
    }

  }

}
