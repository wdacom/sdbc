package com.wda.sdbc.h2

import java.sql.PreparedStatement

import com.wda.sdbc.base.{ParameterValue, Row}

trait SerializeSetter {
  self: ParameterValue with Row with SerializeParameterValue =>

  implicit class QSerialize[T <: AnyRef](override val value: Serialize)
    extends ParameterValue[Serialize] {
    override def asJDBCObject: AnyRef = value.value

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, value)
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

}
