package com.rocketfuel.sdbc.h2.jdbc.implementation

import java.sql.{PreparedStatement, Types}
import com.rocketfuel.sdbc.h2.jdbc.Serialized
import com.rocketfuel.sdbc.base.jdbc._

trait SerializedParameter {

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
      override def update(row: UpdatableRow, columnIndex: Int, x: Serialized): Unit = {
        row.updateObject(columnIndex, x.value, Types.JAVA_OBJECT)
      }
    }

  implicit val SerializedGetter: Getter[Serialized] =
    new Getter[Serialized] {
      override def apply(row: MutableRow, index: Index): Option[Serialized] = {
        Option(row.getObject(index(row))).map(o => Serialized(o.asInstanceOf[AnyRef with java.io.Serializable]))
      }
    }

}
