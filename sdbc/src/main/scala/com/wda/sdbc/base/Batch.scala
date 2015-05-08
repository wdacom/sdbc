package com.wda.sdbc.base

import java.sql.SQLException

import com.wda.Logging
import scala.language.reflectiveCalls

import scala.collection.immutable.Seq

trait Batch {
  self: Connection with ParameterValue with AbstractQuery =>

  trait SetNull {
    def setNull(statement: PreparedStatement, parameterIndex: Int): Unit
  }

  trait AddBatch {
    def addBatch(statement: PreparedStatement): Unit
  }

  trait ExecuteBatch {
    def executeBatch(statement: PreparedStatement)(implicit connection: Connection): Seq[Int]

    def executeLargeBatch(statement: PreparedStatement)(implicit connection: Connection): Seq[Long]
  }

  case class Batch private(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]],
    batches: Seq[Map[String, Option[ParameterValue[_]]]]
  ) extends Logging {

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

    private def setParameters(nameValuePairs: (String, Option[ParameterValue[_]])*): Map[String, Option[ParameterValue[_]]] = {
      nameValuePairs.foldLeft(parameterValues)(setParameter)
    }

    /**
     * Add additional parameter values to the current batch.
     * @param partialBatch
     * @return
     */
    def on(partialBatch: (String, Option[ParameterValue[_]])*): Batch = {
      val newValues = setParameters(partialBatch: _*)
      Batch(statement, newValues, batches)
    }

    /**
     * From the current batch parameter values and any additional parameter values, add a batch.
     * @param batch
     * @return
     */
    def addBatch(batch: (String, Option[ParameterValue[_]])*): Batch = {
      val newValues = setParameters(batch: _*)
      Batch(statement, Map.empty, batches :+ newValues)
    }

    def prepare()(implicit connection: Connection, ev0: SetNull, ev1: AddBatch, ev2: Preparer): PreparedStatement = {
      val prepared = ev2.prepare(connection, queryText)

      for (batch <- batches) {
        for ((parameterName, parameterIndexes) <- statement.parameterPositions) {
          val parameterValue = batch.getOrElse(
            parameterName,
            throw new SQLException(s"No value found for parameter $parameterName.")
          )
          for (parameterIndex <- parameterIndexes) {
            parameterValue match {
              case None => ev0.setNull(prepared, parameterIndex)
              case Some(sqlValue) =>
                sqlValue.set(prepared, parameterIndex)
            }
          }
        }
       ev1.addBatch(prepared)
      }
      prepared
    }

    def withPreparedStatement[U](f: PreparedStatement => U)(implicit connection: Connection, ev0: Closable[PreparedStatement], ev1: Preparer): U = {
      val statement = ev1.prepare(connection, queryText)
      try {
        f(statement)
      } finally {
        //Close the result set, but don't throw any errors if it's already closed.
        ev0.closeQuietly(statement)
      }
    }

    def batch()(implicit connection: Connection, ev0: ExecuteBatch, ev1: Closable[PreparedStatement], ev2: Preparer): Seq[Int] = {
      logger.debug(s"""Executing a batch using "${statement.originalQueryText}".""")
      withPreparedStatement[Seq[Int]](ev0.executeBatch)
    }

    def largeBatch()(implicit connection: Connection, ev0: ExecuteBatch, ev1: Closable[PreparedStatement], ev2: Preparer): Seq[Long] = {
      logger.debug(s"""Executing a large batch using "${statement.originalQueryText}".""")
      withPreparedStatement[Seq[Long]](ev0.executeLargeBatch)
    }
  }

  object Batch {
    def apply(
      query: String,
      hasParameters: Boolean = true
    ): Batch = {
      val statement = CompiledStatement(query, hasParameters)
      new Batch(
        statement,
        Map.empty,
        Vector.empty
      )
    }
  }

}
