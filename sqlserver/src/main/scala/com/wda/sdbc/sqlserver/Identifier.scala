package com.wda.sdbc.sqlserver

import com.wda.sdbc.base
import com.wda.CISet

import scala.util.matching.Regex

class Identifier extends base.Identifier {
  override val leftQuote = '['
  override val rightQuote = ']'

  //https://msdn.microsoft.com/en-us/library/ms175874%28v=sql.105%29.aspx
  override val identifierMatcher: Regex = """(?U)[\p{L}_][\p{L}\d@$#_]*""".r

  //https://msdn.microsoft.com/en-us/library/ms189822.aspx
  override val reservedWords: Set[String] =
    CISet(
      "ADD",
      "ALL",
      "ALTER",
      "AND",
      "ANY",
      "AS",
      "ASC",
      "AUTHORIZATION",
      "BACKUP",
      "BEGIN",
      "BETWEEN",
      "BREAK",
      "BROWSE",
      "BULK",
      "BY",
      "CASCADE",
      "CASE",
      "CHECK",
      "CHECKPOINT",
      "CLOSE",
      "CLUSTERED",
      "COALESCE",
      "COLLATE",
      "COLUMN",
      "COMMIT",
      "COMPUTE",
      "CONSTRAINT",
      "CONTAINS",
      "CONTAINSTABLE",
      "CONTINUE",
      "CONVERT",
      "CREATE",
      "CROSS",
      "CURRENT",
      "CURRENT_DATE",
      "CURRENT_TIME",
      "CURRENT_TIMESTAMP",
      "CURRENT_USER",
      "CURSOR",
      "DATABASE",
      "DBCC",
      "DEALLOCATE",
      "DECLARE",
      "DEFAULT",
      "DELETE",
      "DENY",
      "DESC",
      "DISK",
      "DISTINCT",
      "DISTRIBUTED",
      "DOUBLE",
      "DROP",
      "DUMP",
      "ELSE",
      "END",
      "ERRLVL",
      "ESCAPE",
      "EXCEPT",
      "EXEC",
      "EXECUTE",
      "EXISTS",
      "EXIT",
      "EXTERNAL",
      "FETCH",
      "FILE",
      "FILLFACTOR",
      "FOR",
      "FOREIGN",
      "FREETEXT",
      "FREETEXTTABLE",
      "FROM",
      "FULL",
      "FUNCTION",
      "GOTO",
      "GRANT",
      "GROUP",
      "HAVING",
      "HOLDLOCK",
      "IDENTITY",
      "IDENTITY_INSERT",
      "IDENTITYCOL",
      "IF",
      "IN",
      "INDEX",
      "INNER",
      "INSERT",
      "INTERSECT",
      "INTO",
      "IS",
      "JOIN",
      "KEY",
      "KILL",
      "LEFT",
      "LIKE",
      "LINENO",
      "LOAD",
      "MERGE",
      "NATIONAL",
      "NOCHECK",
      "NONCLUSTERED",
      "NOT",
      "NULL",
      "NULLIF",
      "OF",
      "OFF",
      "OFFSETS",
      "ON",
      "OPEN",
      "OPENDATASOURCE",
      "OPENQUERY",
      "OPENROWSET",
      "OPENXML",
      "OPTION",
      "OR",
      "ORDER",
      "OUTER",
      "OVER",
      "PERCENT",
      "PIVOT",
      "PLAN",
      "PRECISION",
      "PRIMARY",
      "PRINT",
      "PROC",
      "PROCEDURE",
      "PUBLIC",
      "RAISERROR",
      "READ",
      "READTEXT",
      "RECONFIGURE",
      "REFERENCES",
      "REPLICATION",
      "RESTORE",
      "RESTRICT",
      "RETURN",
      "REVERT",
      "REVOKE",
      "RIGHT",
      "ROLLBACK",
      "ROWCOUNT",
      "ROWGUIDCOL",
      "RULE",
      "SAVE",
      "SCHEMA",
      "SECURITYAUDIT",
      "SELECT",
      "SEMANTICKEYPHRASETABLE",
      "SEMANTICSIMILARITYDETAILSTABLE",
      "SEMANTICSIMILARITYTABLE",
      "SESSION_USER",
      "SET",
      "SETUSER",
      "SHUTDOWN",
      "SOME",
      "STATISTICS",
      "SYSTEM_USER",
      "TABLE",
      "TABLESAMPLE",
      "TEXTSIZE",
      "THEN",
      "TO",
      "TOP",
      "TRAN",
      "TRANSACTION",
      "TRIGGER",
      "TRUNCATE",
      "TRY_CONVERT",
      "TSEQUAL",
      "UNION",
      "UNIQUE",
      "UNPIVOT",
      "UPDATE",
      "UPDATETEXT",
      "USE",
      "USER",
      "VALUES",
      "VARYING",
      "VIEW",
      "WAITFOR",
      "WHEN",
      "WHERE",
      "WHILE",
      "WITH",
      "WITHIN GROUP",
      "WRITETEXT"
    )
}