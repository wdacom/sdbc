package com.wda.sdbc.base

import java.sql.SQLException

import com.wda.Logging
import scala.language.reflectiveCalls

import scala.collection.immutable.Seq

trait Batch[
  QueryResult,
  WrappedConnection <: {def close(): Unit},
  PreparedStatement <: {def close(): Unit; def execute(): Unit; def setNull(parameterIndex: Int): Unit; def addBatch(): Unit; def executeBatch(): Array[Int]; def executeLargeBatch(): Array[Long]},
  WrappedRow
] {
  self: Connection[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] with ParameterValue[WrappedRow, PreparedStatement] with AbstractQuery[QueryResult, WrappedConnection, PreparedStatement, WrappedRow] =>

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

    def prepare()(implicit connection: Connection): PreparedStatement = {
      val prepared = connection.prepareStatement(queryText)

      for (batch <- batches) {
        for ((parameterName, parameterIndexes) <- statement.parameterPositions) {
          val parameterValue = batch.getOrElse(
            parameterName,
            throw new SQLException(s"No value found for parameter $parameterName.")
          )
          for (parameterIndex <- parameterIndexes) {
            parameterValue match {
              case None => prepared.setNull(parameterIndex)
              case Some(sqlValue) =>
                sqlValue.set(prepared, parameterIndex)
            }
          }
        }
        prepared.addBatch()
      }
      prepared
    }

    def withPreparedStatement[U](f: PreparedStatement => U)(implicit connection: Connection): U = {
      val statement = prepare()
      try {
        f(statement)
      } finally {
        //Close the result set, but don't throw any errors if it's already closed.
        util.Try(statement.close())
      }
    }

    def batch()(implicit connection: Connection): Seq[Int] = {
      logger.debug(s"""Executing a batch using "${statement.originalQueryText}".""")
      withPreparedStatement[Seq[Int]] { prepared =>
        prepared.executeBatch().toVector
      }
    }

    def largeBatch()(implicit connection: Connection): Seq[Long] = {
      logger.debug(s"""Executing a large batch using "${statement.originalQueryText}".""")
      withPreparedStatement[Seq[Long]] { prepared =>
        prepared.executeLargeBatch().toVector
      }
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
