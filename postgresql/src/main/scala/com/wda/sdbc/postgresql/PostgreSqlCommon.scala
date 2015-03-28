package com.wda.sdbc.postgresql

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}

import com.wda.sdbc.base._
import com.wda.sdbc.{DBMS, base}
import org.postgresql.PGConnection

import scala.collection.immutable.Seq

/**
 * Created by Jeff on 3/27/2015.
 */
abstract class PostgreSqlCommon
  extends DBMS
  with ParameterValues
  with HasJava8TimeFormatter
  with HasJava8DateTimeFormatter
  with IntervalImplicits
  with Getters
  with Setters {

  override def dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
  override def driverClassName = "org.postgresql.Driver"
  override def jdbcScheme = "postgresql"
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
    )(implicit conversion: Row => T
    ): Select[T] = {
    val queryText =
      s"""INSERT INTO ${Identifier.quote(tableSchema, tableName)}
          |${QueryBuilder.columnNames(columnOrder, defaults)}
          |VALUES
          |${QueryBuilder.columnValues(columnOrder, defaults)}}
                                                                |RETURNING *
       """.stripMargin
    Select[T](queryText)
  }

  type LTree = com.wda.sdbc.postgresql.LTree

  val LTree = com.wda.sdbc.postgresql.LTree

}
