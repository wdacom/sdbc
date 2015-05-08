package com.wda.sdbc.postgresql

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.wda.sdbc.base._
import com.wda.sdbc.jdbc.DBMS
import com.wda.sdbc.base
import org.postgresql.PGConnection

/**
 * Created by Jeff on 3/27/2015.
 */
abstract class PostgreSqlCommon
  extends DBMS
  with Setters
  with HasJava8TimeFormatter
  with HasJava8DateTimeFormatter
  with IntervalImplicits
  with ConnectionImplicits
  with Getters {

  override def dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
  override def driverClassName = "org.postgresql.Driver"
  override def jdbcSchemes = Set("postgresql")
  override def productName: String = "PostgreSQL"
  override val supportsIsValid: Boolean = true

  override val Identifier: base.Identifier = new Identifier

  override val timeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_TIME).
    optionalStart().
    appendOffset("+HH:mm", "+00").
    optionalEnd().
    toFormatter
  }

  override val dateTimeFormatter: DateTimeFormatter = {
    new DateTimeFormatterBuilder().
    parseCaseInsensitive().
    append(DateTimeFormatter.ISO_LOCAL_DATE).
    appendLiteral(' ').
    append(timeFormatter).
    toFormatter
  }

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

}
