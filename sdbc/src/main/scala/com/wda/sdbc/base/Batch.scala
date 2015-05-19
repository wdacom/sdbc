package com.wda.sdbc.base

import java.sql.SQLException

import com.wda.Logging
import scala.collection.immutable.Seq

protected trait BatchMethods[UnderlyingConnection, UnderlyingQuery] {

  def setNull(statement: UnderlyingQuery, parameterIndex: Int): Unit

  def addBatch(statement: UnderlyingQuery): Unit

  def executeBatch(statement: UnderlyingQuery)(implicit connection: UnderlyingConnection): Seq[Long]

  def isClosableConnection: Closable[UnderlyingConnection]

  def isClosableStatement: Closable[UnderlyingQuery]

}

case class Batch[UnderlyingConnection, Execute, Select, Update, UnderlyingResultSet, UnderlyingRow] private(
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]],
  batches: Seq[Map[String, Option[ParameterValue[_, UnderlyingQuery]]]]
)(implicit isBatch: BatchMethods[UnderlyingConnection, UnderlyingQuery],
  isQuery: QueryMethods[[UnderlyingConnection, Execute, Select, Update, UnderlyingResultSet, UnderlyingRow]]
) extends Logging {

  def queryText = statement.queryText

  def originalQueryText = statement.originalQueryText

  def parameterPositions = statement.parameterPositions

  private def setParameter(
    parameterValues: Map[String, Option[ParameterValue[_, UnderlyingQuery]]],
    nameValuePair: (String, Option[ParameterValue[_, UnderlyingQuery]])
  ): Map[String, Option[ParameterValue[_, UnderlyingQuery]]] = {
    if (statement.parameterPositions.contains(nameValuePair._1)) {
      parameterValues + nameValuePair
    } else {
      throw new IllegalArgumentException(s"${nameValuePair._1} is not a parameter in the query.")
    }
  }

  private def setParameters(nameValuePairs: (String, Option[ParameterValue[_, UnderlyingQuery]])*): Map[String, Option[ParameterValue[_, UnderlyingQuery]]] = {
    nameValuePairs.foldLeft(parameterValues)(setParameter)
  }

  /**
   * Add additional parameter values to the current batch.
   * @param partialBatch
   * @return
   */
  def on(partialBatch: (String, Option[ParameterValue[_, UnderlyingQuery]])*): Batch[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow] = {
    val newValues = setParameters(partialBatch: _*)
    Batch(statement, newValues, batches)
  }

  /**
   * From the current batch parameter values and any additional parameter values, add a batch.
   * @param batch
   * @return
   */
  def addBatch(batch: (String, Option[ParameterValue[_, UnderlyingQuery]])*): Batch[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow] = {
    val newValues = setParameters(batch: _*)
    Batch(statement, Map.empty, batches :+ newValues)
  }

  def prepare()(implicit connection: UnderlyingConnection,
    queryMethods: ParameterizedQueryMethods[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow]
  ): UnderlyingQuery = {
   val compiled = queryMethods.compile(connection, statement.queryText)

    for (batch <- batches) {
      for ((parameterName, parameterIndexes) <- statement.parameterPositions) {
        val parameterValue = batch.getOrElse(
          parameterName,
          throw new SQLException(s"No value found for parameter $parameterName.")
        )
        for (parameterIndex <- parameterIndexes) {
          parameterValue match {
            case None => isBatch.setNull(compiled, parameterIndex)
            case Some(sqlValue) =>
              sqlValue.set(compiled, parameterIndex)
          }
        }
      }
      isBatch.addBatch(compiled)
    }

    compiled
  }

  def withPreparedStatement[U](
    f: UnderlyingQuery => U
  )(implicit connection: UnderlyingConnection,
    queryMethods: ParameterizedQueryMethods[UnderlyingConnection, UnderlyingQuery, UnderlyingResultSet, UnderlyingRow]
  ): U = {
    val compiled = queryMethods.compile(connection, statement.queryText)
    try {
      f(compiled)
    } finally {
      //Close the result set, but don't throw any errors if it's already closed.
      isBatch.isClosableStatement.closeQuietly(compiled)
    }
  }

  def batch()(implicit connection: UnderlyingConnection): Seq[Long] = {
    logger.debug( s"""Executing a batch using "${statement.originalQueryText}".""")
    withPreparedStatement[Seq[Long]](isBatch.executeBatch)
  }

}

object Batch {
  def apply[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow](
    query: String,
    hasParameters: Boolean = true
  )(implicit isBatch: BatchMethods[UnderlyingConnection, PreparedStatement],
    isConnection: QueryMethods[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow]
  ): Batch[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] = {
    val statement = CompiledStatement(query, hasParameters)
    new Batch(
      statement,
      Map.empty,
      Vector.empty
    )
  }
}
