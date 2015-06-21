package com.wda.sdbc.jdbc

import java.io.Closeable
import java.sql._

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement

case class Select[T] private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]]
)(implicit val converter: Row => T)
  extends base.Select[T, Connection]
  with ParameterizedQuery[Select[T]]
  with ResultSetImplicits
  with Logging {

  private def executeQuery()(implicit connection: Connection): ResultSet = {
    logger.debug(s"""Retrieving a ResultSet using "$originalQueryText" with parameters $parameterValues.""")

    val prepared = prepare(
      queryText = queryText,
      parameterValues = parameterValues,
      parameterPositions = parameterPositions
    )

    prepared.executeQuery()
  }

  /**
   * Retrieve a result set as an iterator of values. You can close the iterator at any time.
   * The iterator will close the underlying ResultSet after retrieving the final row.
   * @param connection
   * @return
   */
  override def iterator()(implicit connection: Connection): Iterator[T] with Closeable = {
    logger.debug(s"""Retrieving an iterator using "$originalQueryText" with parameters $parameterValues.""")

    new Iterator[T] with Closeable {
      private val iterator = executeQuery().iterator()

      override def hasNext: Boolean = iterator.hasNext

      //Calling .map(converter) would be easier, but it wouldn't expose the close() method.
      override def next(): T = converter(iterator.next())

      override def close(): Unit = iterator.close()
    }
  }

  /**
   * Gets the first row from the result set, if one exists.
   * @param connection
   * @return
   */
  def option()(implicit connection: Connection): Option[T] = {
    logger.debug(s"""Retrieving a value using "$originalQueryText" with parameters $parameterValues.""")

    val iterator = executeQuery().iterator()

    val value = iterator.map(converter).toStream.headOption

    iterator.close()

    value
  }

  override def execute()(implicit connection: Connection): Unit = {
    logger.debug(s"""Retrieving an iterator using "$originalQueryText" with parameters $parameterValues.""")

    val prepared = prepare(
      queryText = queryText,
      parameterValues = parameterValues,
      parameterPositions = parameterPositions
    )

    prepared.execute()

    prepared.close()
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Select[T] = {
    Select[T](
      statement,
      parameterValues
    )
  }
}

object Select {
  def apply[T](
    queryText: String,
    hasParameters: Boolean = true
  )(implicit converter: Row => T
  ): Select[T] = {
    Select[T](
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]]
    )
  }
}
