package com.wda.sdbc.h2

import java.sql.PreparedStatement

import com.wda.sdbc.base.{Getter, ParameterValue, Row}

/**
 * Serialized forces the user to tell the H2 client to send the parameter
 * as a serialized object instead of trying the other implicit conversions
 * to ParameterValue.
 */
trait SerializedParameter {

  case class Serialized(
    value: Serializable
  )

}

trait SerializedSetter {
  self: SerializedParameter with ParameterValue with Getter with Row =>

  implicit class QSerialized[T <: Serializable](override val value: Serialized)
    extends ParameterValue[Serialized] {

    override def asJDBCObject: AnyRef = {
      new AbstractMethodError("Serializable objects are not necessarily JDBC objects.")
    }

    override def update(
      row: Row,
      columnIndex: Int
    ): Unit = {
      row
      .updateObject(
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

}

trait SerializedGetter {
  self: SerializedParameter with Row with Getter =>

  implicit val SerializeGetter: Getter[Serialized] =
    new Getter[Serialized] {
      override def apply(row: Row, columnIndex: Int): Option[Serialized] = {
        Option(row.getObject(columnIndex)).map(o => Serialized(o.asInstanceOf[Serializable]))
      }
    }

}
