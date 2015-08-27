package com.rocketfuel.sdbc.base.jdbc

import java.sql.ResultSetMetaData

import com.rocketfuel.CaseInsensitiveOrdering

abstract class Row {

  def columnTypes: IndexedSeq[String]

  def columnNames: IndexedSeq[String]

  def columnIndex(columnName: String): Index = {
    IntIndex(findColumn(columnName))
  }

  def asStringMap(implicit getter: Getter[ParameterValue[_]]): Map[String, Option[ParameterValue[_]]] = {
    asIntMap.zipWithIndex.map {
      case (value, ix) =>
        val columnName = columnNames(ix)
        columnName -> value
    }.toMap
  }

  def asIntMap(implicit getter: Getter[ParameterValue[_]]): IndexedSeq[Option[ParameterValue[_]]]

  /**
   * The index of the row in the result set.
   * @return
   */
  def getRow: Int

  def getMetaData: ResultSetMetaData

  def findColumn(columnLabel: String): Int = {
    for (i <- columnNames.indices) {
      if (CaseInsensitiveOrdering.compare(columnNames(i), columnLabel) == 0) {
        return i
      }
    }
    -1
  }

}
