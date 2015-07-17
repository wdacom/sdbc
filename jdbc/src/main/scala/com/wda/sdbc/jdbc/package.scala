package com.wda.sdbc

import java.sql.{Types, Connection, PreparedStatement}

package object jdbc
  extends HikariImplicits
  with ResultSetImplicits {

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, PreparedStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, PreparedStatement, Int]

  type Index = PartialFunction[Row, Int]

  type Getter[+T] = base.Getter[Row, Index, T]

  private[jdbc] def prepare(
    queryText: String,
    parameterValues: Map[String, Option[ParameterValue[_]]],
    parameterPositions: Map[String, Set[Int]]
  )(implicit connection: Connection
  ): PreparedStatement = {
    val preparedStatement = connection.prepareStatement(queryText)

    for ((key, maybeValue) <- parameterValues) {
      val parameterIndices = parameterPositions(key)

      maybeValue match {
        case None =>
          for (parameterIndex <- parameterIndices) {
            preparedStatement.setNull(parameterIndex, Types.NULL)
          }
        case Some(value) =>
          for (parameterIndex <- parameterIndices) {
            value.set(preparedStatement, parameterIndex)
          }
      }
    }

    preparedStatement
  }

}
