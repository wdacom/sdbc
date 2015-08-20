package com.rocketfuel.sdbc.sqlserver.jdbc.implementation

import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}
import com.rocketfuel.sdbc.base.jdbc.{HasDateTimeFormatter, DBMS}

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
  with Setters
  with Getters
  with Updaters
  with HasDateTimeFormatter {

  override def driverClassName = "net.sourceforge.jtds.jdbc.Driver"
  override def dataSourceClassName ="net.sourceforge.jtds.jdbcx.JtdsDataSource"
  override def jdbcSchemes = Set("jtds:sqlserver")
  override def productName: String = "Microsoft SQL Server"
  override val supportsIsValid = false

  override val dateTimeFormatter: DateTimeFormatter = {
      new DateTimeFormatterBuilder().
      append(ISODateTimeFormat.date()).
      appendLiteral(' ').
      append(ISODateTimeFormat.hourMinuteSecondFraction()).
      appendLiteral(' ').
      appendTimeZoneOffset("+00:00", "+00:00", true, 2, 2).
      toFormatter
  }

}
