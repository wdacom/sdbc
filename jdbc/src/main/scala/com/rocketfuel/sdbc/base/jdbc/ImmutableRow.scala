package com.rocketfuel.sdbc.base.jdbc

import java.sql.ResultSetMetaData

case class ImmutableRow private[jdbc] (
  override val columnTypes: IndexedSeq[String],
  override val columnNames: IndexedSeq[String],
  parameters: IndexedSeq[Option[ParameterValue[_]]],
  override val getMetaData: ResultSetMetaData,
  override val getRow: Int
) extends Row {

  def get[T](columnIndex: Index): Option[ParameterValue[_]] = {
    parameters(columnIndex(this))
  }

  override def asIntMap(implicit getter: Getter[ParameterValue[_]]): IndexedSeq[Option[ParameterValue[_]]] = {
    parameters
  }

}

object ImmutableRow {
  implicit def apply(row: MutableRow)(implicit getter: Getter[ParameterValue[_]]): ImmutableRow = {
    ImmutableRow(
      columnTypes = row.columnTypes,
      columnNames = row.columnNames,
      parameters = row.asIntMap,
      getMetaData = row.getMetaData,
      getRow = row.getRow
    )
  }
}
