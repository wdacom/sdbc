package com.wda.sdbc
package sqlserver

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.wda.sdbc.base
import com.wda.sdbc.base.HasJava8DateTimeFormatter

import scala.collection.immutable.Seq

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
  with ParameterValues
  with Getters
  with Setters
  with HierarchyId
{
  override def driverClassName = "net.sourceforge.jtds.jdbc.Driver"
  override def dataSourceClassName ="net.sourceforge.jtds.jdbcx.JtdsDataSource"
  override def jdbcScheme = "jtds:sqlserver"
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

  /**
   * Creates an insert statement that returns all the values that were inserted.
   * @param tableSchema
   * @param tableName
   * @param columnOrder
   * @param defaults The columns that are to be inserted with default values.
   * @param conversion
   * @tparam T
   * @return
   */
  override def buildInsert[T](
    tableSchema: String,
    tableName: String,
    columnOrder: Seq[String],
    defaults: Set[String]
  )(implicit conversion: (Row) => T
  ): Select[T] = {
    val queryText =
      s"""INSERT INTO ${Identifier.quote(tableSchema, tableName)}
         |${QueryBuilder.columnNames(columnOrder, defaults)}
         |OUTPUT inserted.*
         |VALUES
         |${QueryBuilder.columnValues(columnOrder, defaults)}}
       """.stripMargin
    Select[T](queryText)
  }
}
