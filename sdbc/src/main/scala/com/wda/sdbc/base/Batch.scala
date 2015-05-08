package com.wda.sdbc.base

import java.sql.SQLException

import com.wda.Logging

import scala.collection.immutable.Seq

trait Batch {
  self: Connection with ParameterValue with AbstractQuery =>

  protected trait BatchMethods {
    def setNull(statement: PreparedStatement, parameterIndex: Int): Unit

    def addBatch(statement: PreparedStatement): Unit

    def executeBatch(statement: PreparedStatement)(implicit connection: UnderlyingConnection): Seq[Int]

    def executeLargeBatch(statement: PreparedStatement)(implicit connection: UnderlyingConnection): Seq[Long]
  }

  protected val isBatch: BatchMethods

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

    def prepare()(implicit connection: UnderlyingConnection): PreparedStatement = {
      val prepared = isConnection.prepare(connection, queryText)

      for (batch <- batches) {
        for ((parameterName, parameterIndexes) <- statement.parameterPositions) {
          val parameterValue = batch.getOrElse(
            parameterName,
            throw new SQLException(s"No value found for parameter $parameterName.")
          )
          for (parameterIndex <- parameterIndexes) {
            parameterValue match {
              case None => isBatch.setNull(prepared, parameterIndex)
              case Some(sqlValue) =>
                sqlValue.set(prepared, parameterIndex)
            }
          }
        }
        isBatch.addBatch(prepared)
      }
      prepared
    }

    def withPreparedStatement[U](f: PreparedStatement => U)(implicit connection: UnderlyingConnection): U = {
      val statement = isConnection.prepare(connection, queryText)
      try {
        f(statement)
      } finally {
        //Close the result set, but don't throw any errors if it's already closed.
        isClosablePreparedStatement.closeQuietly(statement)
      }
    }

    def batch()(implicit connection: UnderlyingConnection): Seq[Int] = {
      logger.debug( s"""Executing a batch using "${statement.originalQueryText}".""")
      withPreparedStatement[Seq[Int]](isBatch.executeBatch)
    }

    def largeBatch()(implicit connection: UnderlyingConnection): Seq[Long] = {
      logger.debug( s"""Executing a large batch using "${statement.originalQueryText}".""")
      withPreparedStatement[Seq[Long]](isBatch.executeLargeBatch)
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
