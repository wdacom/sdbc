package com.wda.sdbc.h2

import java.sql.PreparedStatement

import com.wda.sdbc.jdbc._

/**
 * Serialized forces the user to tell the H2 client to send the parameter
 * as a serialized object instead of trying the other implicit conversions
 * to ParameterValue.
 */
trait OtherParameter {

  case class Other(
    value: AnyRef with java.io.Serializable
  )

  case class QOther(
    override val value: Other
  ) extends ParameterValue[Other] {

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

  implicit def OtherToParameterValue(x: Other): ParameterValue[Other] = {
    new ParameterValue[Other] {
      override val value: Other = x

      override def set(statement: PreparedStatement, parameterIndex: Int): Unit = {
        statement.setObject(parameterIndex, value.value)
      }
    }
  }

  implicit val OtherUpdater: Updater[Other] =
    new Updater[Other] {
      override def update(row: MutableRow, columnIndex: Int, x: Other): Unit = {
        row.updateObject(columnIndex, x.value)
      }
    }

  implicit val OtherGetter: Getter[Other] =
    new Getter[Other] {

      override def apply(row: Row): (Index) => Option[Other] = { index =>
        Option(row.getObject(index(row))).map(o => Other(o.asInstanceOf[AnyRef with java.io.Serializable]))
      }
    }

}
