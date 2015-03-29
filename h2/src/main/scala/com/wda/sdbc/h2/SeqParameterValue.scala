package com.wda.sdbc.h2

import java.io
import java.sql.PreparedStatement

import com.wda.sdbc.base.{Getter, ParameterValue, Row}

trait SeqParameterValue {
  self: Row with Getter with ParameterValue =>

  implicit class QArray(
    override val value: Seq[io.Serializable]
  ) extends ParameterValue[Seq[io.Serializable]] {

    override def asJDBCObject: AnyRef = value.toArray

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, asJDBCObject)
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJDBCObject)
    }

  }

}
