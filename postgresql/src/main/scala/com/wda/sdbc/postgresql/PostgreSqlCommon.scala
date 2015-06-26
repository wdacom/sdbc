package com.wda.sdbc.postgresql

import com.wda.sdbc.base._
import com.wda.sdbc.{DBMS, base}
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatterBuilder, DateTimeFormatter}
import org.postgresql.PGConnection

/**
 * Created by Jeff on 3/27/2015.
 */
abstract class PostgreSqlCommon
  extends DBMS
  with Setters
  with ConnectionImplicits
  with Getters
  with HasDateTimeFormatter
  with DurationImplicits {

  override def dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
  override def driverClassName = "org.postgresql.Driver"
  override def jdbcSchemes = Set("postgresql")
  override def productName: String = "PostgreSQL"
  override val supportsIsValid: Boolean = true

  override val Identifier: base.Identifier = new Identifier

  /**
   * Perform any connection initialization that should be done when a connection
   * is created. EG add a type mapping.
   *
   * By default this method does nothing.
   * @param connection
   */
  override def initializeConnection(connection: java.sql.Connection): Unit = {
    connection.unwrap[PGConnection](classOf[PGConnection]).addDataType("ltree", classOf[LTree])
  }

  type LTree = com.wda.sdbc.postgresql.LTree

  val LTree = com.wda.sdbc.postgresql.LTree

  override val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
    append(ISODateTimeFormat.date()).
    appendLiteral(' ').
    append(ISODateTimeFormat.hourMinuteSecondFraction()).
    appendTimeZoneOffset("+00", "+00", true, 1, 2).
    toFormatter
  }

}
