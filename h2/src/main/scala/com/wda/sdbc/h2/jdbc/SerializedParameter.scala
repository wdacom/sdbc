package com.wda.sdbc.h2.jdbc

import java.sql.{Types, PreparedStatement}

import com.wda.sdbc.base.jdbc._

/**
 * Serialized forces the user to tell the H2 client to send the parameter
 * as a serialized object instead of trying the other implicit conversions
 * to ParameterValue.
 */
trait SerializedParameter {

  case class Serialized(
    value: AnyRef with java.io.Serializable
  )

  case class QSerialized(
    override val value: Serialized
  ) extends ParameterValue[Serialized] {

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

  implicit def SerializedToParameterValue(x: Serialized): ParameterValue[Serialized] = {
    new ParameterValue[Serialized] {
      override val value: Serialized = x

      override def set(statement: PreparedStatement, parameterIndex: Int): Unit = {
        statement.setObject(parameterIndex, value.value, Types.JAVA_OBJECT)
      }
    }
  }

  implicit val SerializedUpdater: Updater[Serialized] =
    new Updater[Serialized] {
      override def update(row: MutableRow, columnIndex: Int, x: Serialized): Unit = {
        row.updateObject(columnIndex, x.value, Types.JAVA_OBJECT)
      }
    }

  implicit val SerializedGetter: Getter[Serialized] =
    new Getter[Serialized] {
      override def apply(row: Row, index: Index): Option[Serialized] = {
        Option(row.getObject(index(row))).map(o => Serialized(o.asInstanceOf[AnyRef with java.io.Serializable]))
      }
    }

}
