package com.wda.sdbc.base

import java.sql.SQLException

import com.wda.Logging
import scala.collection.immutable.Seq

protected trait BatchMethods[UnderlyingConnection, PreparedStatement] {

  def setNull(statement: PreparedStatement, parameterIndex: Int): Unit

  def addBatch(statement: PreparedStatement): Unit

  def executeBatch(statement: PreparedStatement)(implicit connection: UnderlyingConnection): Seq[Int]

  def executeLargeBatch(statement: PreparedStatement)(implicit connection: UnderlyingConnection): Seq[Long]

  def isClosableConnection: Closable[UnderlyingConnection]

  def isClosableStatement: Closable[PreparedStatement]

}

case class Batch[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] private(
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]],
  batches: Seq[Map[String, Option[ParameterValue[_, PreparedStatement]]]]
)(implicit isBatch: BatchMethods[UnderlyingConnection, PreparedStatement],
  isConnection: Connection[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow]
) extends Logging {

  def queryText = statement.queryText

  def parameterPositions = statement.parameterPositions

  private def setParameter(
    parameterValues: Map[String, Option[ParameterValue[_, PreparedStatement]]],
    nameValuePair: (String, Option[ParameterValue[_, PreparedStatement]])
  ): Map[String, Option[ParameterValue[_, PreparedStatement]]] = {
    if (statement.parameterPositions.contains(nameValuePair._1)) {
      parameterValues + nameValuePair
    } else {
      throw new IllegalArgumentException(s"${nameValuePair._1} is not a parameter in the query.")
    }
  }

  private def setParameters(nameValuePairs: (String, Option[ParameterValue[_, PreparedStatement]])*): Map[String, Option[ParameterValue[_, PreparedStatement]]] = {
    nameValuePairs.foldLeft(parameterValues)(setParameter)
  }

  /**
   * Add additional parameter values to the current batch.
   * @param partialBatch
   * @return
   */
  def on(partialBatch: (String, Option[ParameterValue[_, PreparedStatement]])*): Batch[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
    val newValues = setParameters(partialBatch: _*)
    Batch(statement, newValues, batches)
  }

  /**
   * From the current batch parameter values and any additional parameter values, add a batch.
   * @param batch
   * @return
   */
  def addBatch(batch: (String, Option[ParameterValue[_, PreparedStatement]])*): Batch[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
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
      isBatch.isClosableStatement.closeQuietly(statement)
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
  def apply[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow](
    query: String,
    hasParameters: Boolean = true
  )(implicit isBatch: BatchMethods[UnderlyingConnection, PreparedStatement],
    isConnection: Connection[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow]
  )
  : Batch[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
    val statement = CompiledStatement(query, hasParameters)
    new Batch(
      statement,
      Map.empty,
      Vector.empty
    )
  }
}
