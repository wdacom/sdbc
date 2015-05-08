package com.wda.sdbc.h2

import java.sql.PreparedStatement

import com.wda.sdbc.base.{Getter, JdbcParameterValue, Row}

/**
 * Serialized forces the user to tell the H2 client to send the parameter
 * as a serialized object instead of trying the other implicit conversions
 * to ParameterValue.
 */
trait OtherParameter {
  self: JdbcParameterValue with Getter with Row =>

  case class Other(
    value: Serializable
  )

  implicit class QOther[T <: Serializable](
    override val value: Other
  ) extends ParameterValue[Other] {

    override def asJDBCObject: AnyRef = {
      new AbstractMethodError("Serializable objects are not necessarily JDBC objects.")
    }

    override def update(
      row: JdbcRow,
      columnIndex: Int
    ): Unit = {
      row.updateObject(
        columnIndex,
        value.value
      )
    }

    override def set(
      preparedStatement: PreparedStatement,
      parameterIndex: Int
    ): Unit = {
      preparedStatement.setObject(
        parameterIndex,
        value.value
      )
    }

  }

  implicit val OtherGetter: Getter[Other] =
    new Getter[Other] {
      override def apply(row: JdbcRow, columnIndex: Int): Option[Other] = {
        Option(row.getObject(columnIndex)).map(o => Other(o.asInstanceOf[Serializable]))
      }
    }

}
