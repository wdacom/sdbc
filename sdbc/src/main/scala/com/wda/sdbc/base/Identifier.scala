package com.wda.sdbc.base

import scala.util.matching.Regex

abstract class Identifier {
  /**
   * The character that is prepended to an identifier for quoting.
   */
  val leftQuote: Char

  /**
   * The character that is appended to an identifier for quoting.
   */
  val rightQuote: Char

  val identifierMatcher: Regex

  val reservedWords: Set[String]

  def requiresQuoting(identifier: String): Boolean = {
    reservedWords.contains(identifier) ||
      ! identifierMatcher.pattern.matcher(identifier).matches()
  }

  /**
   * Quote a multipart identifier, such as a fully qualified table name.
   */
  def quote(identifiers: String*): String = {
    identifiers.map(quote).mkString(".")
  }

  /**
   * Quote an identifier.
   * @param identifier
   * @return
   */
  @throws[IllegalArgumentException]
  def quote(identifier: String): String = {
    if (identifier.size == 0) {
      throw new IllegalArgumentException("Database identifiers must have nonzero size.")
    }
    if (requiresQuoting(identifier)) {
      val escaped = escape(identifier)
      escaped.insert(0, leftQuote)
      escaped.append(rightQuote)
      escaped.toString()
    } else {
      identifier
    }
  }

  /**
   * Escape an identifier to make it ready for quoting.
   * @param name
   * @return
   */
  private def escape(name: String): StringBuilder = {
    val b = new StringBuilder(name.length)

    for (char <- name) {
      //If the char needs to be escaped, add it twice.
      if (char == rightQuote) {
        b.append(rightQuote)
      }
      b.append(char)
    }

    b
  }
}
