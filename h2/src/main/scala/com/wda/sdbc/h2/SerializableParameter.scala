package com.wda.sdbc.h2

import java.sql.PreparedStatement

import com.wda.sdbc.base.{ParameterValue, Row}

trait SerializableParameter {
  self: ParameterValue with Row =>

  implicit class QSerializable[T <: java.io.Serializable](override val value: T)
    extends ParameterValue[T] {
    override def asJDBCObject: AnyRef = value.asInstanceOf[AnyRef]

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, value)
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, value)
    }
  }

}
