package com.rocketfuel.sdbc.base.jdbc

import java.sql._

import com.rocketfuel.Logging
import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.CompiledStatement

case class Select[T] private [jdbc] (
  override val statement: CompiledStatement,
  override val parameterValues: Map[String, Option[Any]]
)(implicit val converter: Row => T,
  parameterSetter: ParameterSetter
) extends base.Select[Connection, T]
  with ParameterizedQuery[Select[T]]
  with ResultSetImplicits
  with Logging {

  private def executeQuery()(implicit connection: Connection): ResultSet = {
    logger.debug(s"""Selecting "$originalQueryText" with parameters $parameterValues.""")
    val prepared = prepare(
      queryText = queryText,
      parameterValues = parameterValues,
      parameterPositions = parameterPositions
    )

    prepared.executeQuery()
  }

  /**
   * Retrieve a result set as an iterator of values.
   * The iterator will close the underlying ResultSet after retrieving the final row.
   * @param connection
   * @return
   */
  override def iterator()(implicit connection: Connection): Iterator[T] = {
    executeQuery().iterator().map(converter)
  }

  /**
   * Gets the first row from the result set, if one exists.
   * @param connection
   * @return
   */
  override def option()(implicit connection: Connection): Option[T] = {
    val results = executeQuery()

    val iterator = results.iterator()

    val value = iterator.map(converter).toStream.headOption

    results.close()

    value
  }

  override protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[Any]]
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
  )(implicit converter: Row => T,
    parameterSetter: ParameterSetter
  ): Select[T] = {
    Select[T](
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[Any]]
    )
  }
}
