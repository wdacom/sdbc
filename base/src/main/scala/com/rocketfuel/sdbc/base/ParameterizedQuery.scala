package com.rocketfuel.sdbc.base

/**
 * Given a query with named parameters beginning with '$',
 * construct the query for use with JDBC, so that named
 * parameters are replaced by '?', and each parameter
 * has a map to its positions in the query.
 *
 * Identifiers must start with a letter or underscore, and then
 * any character after the first one can be a letter, number,
 * underscore, or '\$'. An identifier that does not follow
 * this scheme must be quoted by backticks.
 *
 * Examples of identifiers:
 * \$hello
 * \$`hello there`
 * \$_i_am_busy
 */
trait ParameterizedQuery[
  Self <: ParameterizedQuery[Self, UnderlyingQuery, Index],
  UnderlyingQuery,
  Index
] {

  def statement: CompiledStatement

  def parameterValues: Map[String, Option[Any]]

  /**
   * The query text with name parameters replaced with positional parameters.
   * @return
   */
  def queryText: String = statement.queryText

  def originalQueryText: String = statement.originalQueryText

  def parameterPositions: Map[String, Set[Int]] = statement.parameterPositions

  private def setParameter(
    parameterValues: Map[String, Option[Any]],
    nameValuePair: (String, Option[ParameterValue])
  ): Map[String, Option[Any]] = {
    if (parameterPositions.contains(nameValuePair._1)) {
      val stripped = nameValuePair._2.map(p => p.value)
      parameterValues + (nameValuePair._1 -> stripped)
    } else {
      throw new IllegalArgumentException(s"${nameValuePair._1} is not a parameter in the query.")
    }
  }

  protected def subclassConstructor(
    statement: CompiledStatement,
    parameterValues: Map[String, Option[Any]]
  ): Self

  def on(parameterValues: (String, Option[ParameterValue])*): Self = {
    val newValues = setParameters(parameterValues: _*)
    subclassConstructor(statement, newValues)
  }

  protected def setParameters(nameValuePairs: (String, Option[ParameterValue])*): Map[String, Option[Any]] = {
    nameValuePairs.foldLeft(parameterValues)(setParameter)
  }

}
