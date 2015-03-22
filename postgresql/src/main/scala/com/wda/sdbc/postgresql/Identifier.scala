package com.wda.sdbc.postgresql

import com.wda.CISet
import com.wda.sdbc.base

import scala.util.matching.Regex

class Identifier extends base.Identifier {
  override val leftQuote = '"'
  override val rightQuote = leftQuote

  //http://www.postgresql.org/docs/9.4/static/sql-syntax-lexical.html
  override val identifierMatcher: Regex = """[\p{L}_][\p{L}\d_$]*""".r

  //Reserved keywords from http://www.postgresql.org/docs/current/static/sql-keywords-appendix.html
  override val reservedWords: Set[String] =
    CISet(
      "ALL",
      "ANALYSE",
      "ANALYZE",
      "AND",
      "ANY",
      "ARRAY",
      "AS",
      "ASC",
      "ASYMMETRIC",
      "BOTH",
      "CASE",
      "CAST",
      "CHECK",
      "COLLATE",
      "COLUMN",
      "CONSTRAINT",
      "CREATE",
      "CURRENT_CATALOG",
      "CURRENT_DATE",
      "CURRENT_ROLE",
      "CURRENT_TIME",
      "CURRENT_TIMESTAMP",
      "CURRENT_USER",
      "DEFAULT",
      "DEFERRABLE",
      "DESC",
      "DISTINCT",
      "DO",
      "ELSE",
      "END",
      "EXCEPT",
      "FALSE",
      "FETCH",
      "FOR",
      "FOREIGN",
      "FROM",
      "GRANT",
      "GROUP",
      "HAVING",
      "IN",
      "INITIALLY",
      "INTERSECT",
      "INTO",
      "LATERAL",
      "LEADING",
      "LIMIT",
      "LOCALTIME",
      "LOCALTIMESTAMP",
      "NOT",
      "NULL",
      "OFFSET",
      "ON",
      "ONLY",
      "OR",
      "ORDER",
      "PLACING",
      "PRIMARY",
      "REFERENCES",
      "RETURNING",
      "SELECT",
      "SESSION_USER",
      "SOME",
      "SYMMETRIC",
      "TABLE",
      "THEN",
      "TO",
      "TRAILING",
      "TRUE",
      "UNION",
      "UNIQUE",
      "USER",
      "USING",
      "VARIADIC",
      "WHEN",
      "WHERE",
      "WINDOW",
      "WITH"
    )
}
