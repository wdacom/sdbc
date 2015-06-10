package com.wda.sdbc.jdbc

import java.sql.{PreparedStatement, Types, Connection}

import com.wda.Logging
import com.wda.sdbc.base
import com.wda.sdbc.base.CompiledStatement

case class Batch private (
  statement: CompiledStatement,
  parameterValues: Map[String, Option[ParameterValue[_]]],
  parameterValueBatches: Seq[Map[String, Option[ParameterValue[_]]]]
) extends base.Batch[Connection]
  with ParameterizedQuery[Batch]
  with Logging {

  def addBatch(parameterValues: (String, Option[ParameterValue[_]])*): Batch = {
    val newBatch = setParameters(parameterValues: _*)

    Batch(
      statement,
      Map.empty,
      parameterValueBatches :+ newBatch
    )
  }

  protected def prepare()(implicit connection: Connection): PreparedStatement = {
    val prepared = connection.prepareStatement(queryText)
    for (batch <- parameterValueBatches) {
      for ((name, value) <- batch) {
        value match {
          case None =>
            for (index <- parameterPositions(name)) {
              prepared.setNull(index, Types.NULL)
            }
          case Some(parameter) =>
            for (index <- parameterPositions(name)) {
              parameter.set(prepared, index)
            }
        }
      }
    }
    prepared
  }

  override def iterator()(implicit connection: Connection): Iterator[Long] = {
    logger.debug(s"""Executing batch "$originalQueryText".""")
    val prepared = prepare()
    val result = prepared.executeLargeBatch()
    prepared.close()
    result.toIterator
  }


  override def execute()(implicit connection: Connection): Unit = {
    logger.debug(s"""Executing batch "$originalQueryText".""")
    val prepared = prepare()
    prepared.execute()
    prepared.close()
  }

  override def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]]
  ): Batch = {
    Batch(statement, parameterValues, Vector.empty)
  }
}

object Batch {
  def apply(
    queryText: String,
    hasParameters: Boolean = true
  ): Batch = {
    Batch(
      statement = CompiledStatement(queryText, hasParameters),
      parameterValues = Map.empty[String, Option[ParameterValue[_]]],
      parameterValueBatches = Vector.empty[Map[String, Option[ParameterValue[_]]]]
    )
  }
}
