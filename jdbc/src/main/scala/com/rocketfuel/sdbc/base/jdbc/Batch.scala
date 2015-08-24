package com.rocketfuel.sdbc.base.jdbc

import java.sql.{PreparedStatement, Types}

import com.rocketfuel.Logging
import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.CompiledStatement

case class Batch private [jdbc] (
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
      prepared.addBatch()
    }
    prepared
  }

  def seq()(implicit connection: Connection): Seq[Long] = {
    logger.debug(s"""Executing batch "$originalQueryText".""")
    val prepared = prepare()
    val result = try {
      prepared.executeLargeBatch()
    } catch {
      case e: UnsupportedOperationException =>
        prepared.executeBatch().map(_.toLong)
    }
    prepared.close()
    result
  }

  override def iterator()(implicit connection: Connection): Iterator[Long] = {
    seq().toIterator
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
