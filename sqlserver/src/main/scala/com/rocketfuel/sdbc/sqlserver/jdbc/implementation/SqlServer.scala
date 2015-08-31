package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.base.jdbc._

/*
Note that in a result set, sql server (or jtds) doesn't do a good job of reporting the types
of values being delivered.

nvarchar could be:
string, date, time, datetime2, datetimeoffset

ntext could be:
string, xml

varbinary could be:
varbinary, hierarchyid
 */
abstract class SqlServer
  extends DBMS
  with HasOffsetDateTimeFormatter
  with Setters
  with Getters
  with Updaters {

  override def driverClassName = "net.sourceforge.jtds.jdbc.Driver"
  override def dataSourceClassName ="net.sourceforge.jtds.jdbcx.JtdsDataSource"
  override def jdbcSchemes = Set("jtds:sqlserver")
  override def productName: String = "Microsoft SQL Server"
  override val supportsIsValid = false

  override val offsetDateTimeFormatter = {
    val formatter = new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_DATE).
    appendLiteral(' ').
    append(DateTimeFormatter.ISO_LOCAL_TIME).
    optionalStart().
    appendLiteral(' ').
    appendOffset("+HH:MM", "+00:00").
    optionalEnd().
    toFormatter

    SdbcOffsetDateTimeFormatter(formatter)
  }

  override implicit val ParameterGetter: Getter[ParameterValue[_]] = {
    case (row: MutableRow, ix: Index) =>
      ???
  }

  override def toParameter(a: Any): Option[jdbc.ParameterValue[_]] = {
    a match {
      case null | None | Some(null) =>
        None
      case Some(a) =>
        Some(toParameter(a)).flatten
      case _ =>
        Some(toSqlServerParameter(a))
    }
  }

}
