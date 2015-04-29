package com.wda.sdbc.cassandra

import com.datastax.driver.core.PreparedStatement
import com.wda.Logging
import com.wda.sdbc.DBMS

abstract class Cassandra extends DBMS {

  trait AbstractQuery[Self <: AbstractQuery[Self]]
    extends Logging {

    protected val statement: PreparedStatement

    val parameterValues: Map[String, Option[ParameterValue[_]]]

    protected def subclassConstructor(
      statement: CompiledStatement,
      parameterValues: Map[String, Option[ParameterValue[_]]]
      ): Self

    def queryText = statement.queryText

    def parameterPositions = statement.parameterPositions

    private def setParameter(
      parameterValues: Map[String, Option[ParameterValue[_]]],
      nameValuePair: (String, Option[ParameterValue[_]])
      ): Map[String, Option[ParameterValue[_]]] = {
      if (statement.parameterPositions.contains(nameValuePair._1)) {
        parameterValues + nameValuePair
      } else {
        throw new IllegalArgumentException(s"${nameValuePair._1} is not a parameter in the query.")
      }
    }

    def on(parameterValues: (String, Option[ParameterValue[_]])*): Self = {
      val newValues = setParameters(parameterValues: _*)
      subclassConstructor(statement, newValues)
    }

    protected def setParameters(nameValuePairs: (String, Option[ParameterValue[_]])*): Map[String, Option[ParameterValue[_]]] = {
      nameValuePairs.foldLeft(parameterValues)(setParameter)
    }

    protected def prepare(
      updatable: Boolean = false
      )(implicit connection: Connection): PreparedStatement = {
      val prepared =
        connection.underlying.prepareStatement(
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

    /**
     * Perform some action on a Prepared Statement, being sure to close it.
     * Do not use this method when the result depends on an open result, such as if f returned an iterator.
     * @param f
     * @param connection
     * @tparam U
     * @return
     */
    protected def withPreparedStatement[U](f: PreparedStatement => U)(implicit connection: Connection): U = {
      val statement = prepare()
      try {
        f(statement)
      } finally {
        //Close the result set, but don't throw any errors if it's already closed.
        util.Try(statement.close())
      }
    }

    def execute()(implicit connection: Connection): Unit = {
      logger.debug(s"""Executing the query "${statement.originalQueryText}" with parameters $parameterValues.""")
      withPreparedStatement(_.execute())(connection)
    }

  }

}
