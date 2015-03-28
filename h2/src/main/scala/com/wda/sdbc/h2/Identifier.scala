package com.wda.sdbc.h2

import com.wda.sdbc.base

import scala.util.matching.Regex

class Identifier
  extends base.Identifier {
  //http://www.h2database.com/html/grammar.html#quoted_name
  override val leftQuote: Char = '"'
  override val rightQuote: Char = leftQuote

  //http://www.h2database.com/html/grammar.html#name
  override val identifierMatcher: Regex = """(?U)[\p{L}_][\p{L}\d_]*""".r

  //http://www.h2database.com/html/advanced.html#compatibility
  override val reservedWords: Set[String] =
    Set(
      "CROSS",
      "CURRENT_DATE",
      "CURRENT_TIME",
      "CURRENT_TIMESTAMP",
      "DISTINCT",
      "EXCEPT",
      "EXISTS",
      "FALSE",
      "FETCH",
      "FOR",
      "FROM",
      "FULL",
      "GROUP",
      "HAVING",
      "INNER",
      "INTERSECT",
      "IS",
      "JOIN",
      "LIKE",
      "LIMIT",
      "MINUS",
      "NATURAL",
      "NOT",
      "NULL",
      "OFFSET",
      "ON",
      "ORDER",
      "PRIMARY",
      "ROWNUM",
      "SELECT",
      "SYSDATE",
      "SYSTIME",
      "SYSTIMESTAMP",
      "TODAY",
      "TRUE",
      "UNION",
      "UNIQUE",
      "WHERE"
    )
}
