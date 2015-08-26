package com.rocketfuel.sdbc.base.jdbc

import java.sql.ResultSetMetaData

abstract class Row {

  def columnTypes: IndexedSeq[String]

  def columnNames: IndexedSeq[String]

  def columnIndex(columnName: String): Index = {
    IntIndex(columnNames.indexOf(columnName))
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
    columnNames.indexOf(columnLabel)
  }

}
