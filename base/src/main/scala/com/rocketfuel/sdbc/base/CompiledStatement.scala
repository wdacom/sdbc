package com.rocketfuel.sdbc.base

import scala.collection.immutable.Seq

case class CompiledStatement private (
  queryText: String,
  originalQueryText: String,
  parameterPositions: Map[String, Set[Int]]
)

object CompiledStatement {

  def apply(queryText: String, hasParameters: Boolean = true): CompiledStatement = {
    if (hasParameters) {
      val parts = findParts(queryText)
      val positions = findParameterPositions(parts)
      val statement = partsToStatement(parts)
      CompiledStatement(statement, queryText, positions)
    } else {
      CompiledStatement(queryText, queryText, Map.empty[String, Set[Int]])
    }
  }

  /**
   * Since the original variable names can't be gotten from
   * the string context (they're replaced by empty strings),
   * use numbers to represent the parameter names, starting
   * from 0.
   * @param sc
   * @return
   */
  def apply(sc: StringContext): CompiledStatement = {
    val builder = new StringBuilder()
    val parts = sc.parts.iterator
    var i = 0

    builder.append(StringContext.treatEscapes(parts.next()))

    while(parts.hasNext) {
      builder.append(s"@`$i`")
      i += 1
      builder.append(StringContext.treatEscapes(parts.next()))
    }

    val queryText = builder.toString

    apply(queryText)
  }

  //http://www.postgresql.org/docs/9.4/static/sql-syntax-lexical.html
  //We use '@' to signal the beginning of named parameter.
  //Parameters are optionally quoted by backticks. Quoted parameters can not contain backticks.
  //Two '@' in a row are ignored.
  private val parameterMatcher =
    """(?U)@(?<!\@\@)(?:`([^`]+)`|([\p{L}_][\p{L}\p{N}_$]*))""".r

  /**
   * Split the string into its parts up to and including the first
   * parameter, and then the parts after the parameter.
   * @param value
   * @return
   */
  private def nextParameter(value: String): (Seq[QueryPart], Option[String]) = {
    parameterMatcher.findFirstMatchIn(value) match {
      case None =>
        (Seq(QueryText(value)), None)
      case Some(m) =>
        val beforeParameter = {
          if (m.start == 0) {
            Vector.empty
          } else {
            Vector(QueryText(value.substring(0, m.start)))
          }
        }

        val parameter = Option(m.group(1)).getOrElse(m.group(2))

        val afterParameter = {
          if (m.end == value.length)
            None
          else {
            Some(value.substring(m.end, value.length))
          }
        }

        (beforeParameter :+ Parameter(parameter), afterParameter)
    }
  }

  private def findParts(value: String): Seq[QueryPart] = {
    nextParameter(value) match {
      case (parts, None) =>
        parts
      case (parts, Some(remainingText)) =>
        parts ++ findParts(remainingText)
    }
  }

  private def findParameterPositions(parts: Seq[QueryPart]): Map[String, Set[Int]] = {
    parts.collect{case p: Parameter => p}.zipWithIndex.foldLeft(Map.empty[String, Set[Int]]){
      case (positionMap, (Parameter(name), index0)) =>
        val index1 = index0 + 1
        positionMap.get(name) match {
          case None =>
            positionMap + (name -> Set(index1))
          case Some(parameterPositions) =>
            positionMap + (name -> (parameterPositions + index1))
        }
    }
  }

  private def partsToStatement(parts: Seq[QueryPart]): String = {
    parts.map {
      case _: Parameter => "?"
      case QueryText(t) => t
    }.mkString
  }
}
