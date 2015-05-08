package com.wda.sdbc.jdbc

import java.sql.{Connection => JConnection, SQLException, PreparedStatement}

import com.wda.sdbc.base

trait JdbcAbstractQuery extends base.AbstractQuery[JConnection, PreparedStatement] {
  self: JdbcConnection with JdbcParameterValue =>

  abstract class JdbcAbstractQuery[Self <: AbstractQuery[Self]] extends AbstractQuery[Self] {

    override protected def prepare(
      updatable: Boolean = false
    )(implicit connection: Connection[JConnection]): PreparedStatement = {
      val prepared =
        connection.prepareStatement(
          queryText,
          java.sql.ResultSet.TYPE_FORWARD_ONLY,
          if (updatable) java.sql.ResultSet.CONCUR_UPDATABLE else java.sql.ResultSet.CONCUR_READ_ONLY
        )

      for ((parameterName, parameterIndexes) <- parameterPositions) {
        val parameterValue = parameterValues.getOrElse(parameterName,
          throw new SQLException(s"No value found for parameter $parameterName.")
        )
        for (parameterIndex <- parameterIndexes) {
          parameterValue match {
            case None => prepared.setObject(parameterIndex, null)
            case Some(sqlValue) =>
              sqlValue.set(prepared, parameterIndex)
          }
        }
      }
      prepared
    }

  }
  
}
