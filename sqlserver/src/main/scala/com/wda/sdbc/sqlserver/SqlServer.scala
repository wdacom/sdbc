package com.wda.sdbc
package sqlserver

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.wda.sdbc.base.HasJava8DateTimeFormatter
import com.wda.sdbc.jdbc.DBMS

/*
Note that in a result set, sql server (or jtds) doesn't do a good job of reporting the types
of values being delievered.

nvarchar could be:
string, date, time, datetime2, datetimeoffset

ntext could be:
string, xml

varbinary could be:
varbinary, hierarchyid
 */
abstract class SqlServer
  extends DBMS
  with HasJava8DateTimeFormatter
  with Setters
  with Getters
  with HierarchyId
{
  override def driverClassName = "net.sourceforge.jtds.jdbc.Driver"
  override def dataSourceClassName ="net.sourceforge.jtds.jdbcx.JtdsDataSource"
  override def jdbcSchemes = Set("jtds:sqlserver")
  override def productName: String = "Microsoft SQL Server"
  override val supportsIsValid = false

  override val Identifier: base.Identifier = new Identifier

  override val dateTimeFormatter = {
    new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_DATE).
    appendLiteral(' ').
    append(DateTimeFormatter.ISO_LOCAL_TIME).
    optionalStart().
    appendLiteral(' ').
    appendOffset("+HH:MM", "+00:00").
    optionalEnd().
    toFormatter
  }
}
