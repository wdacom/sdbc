package com.wda.sdbc.h2

import java.sql.PreparedStatement

import com.wda.sdbc.base.{Getter, ParameterValue, Row}

import scala.reflect.ClassTag

trait SeqParameterValue {
  self: Row with Getter with ParameterValue =>

  implicit class QArray[T](
    override val value: Seq[T]
  )(implicit classTag: ClassTag[T]) extends ParameterValue[Seq[T]] {

    override def asJDBCObject: AnyRef = value.toArray

    override def update(row: Row, columnIndex: Int): Unit = {
      row.updateObject(columnIndex, asJDBCObject)
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      preparedStatement.setObject(parameterIndex, asJDBCObject)
    }

  }

}
