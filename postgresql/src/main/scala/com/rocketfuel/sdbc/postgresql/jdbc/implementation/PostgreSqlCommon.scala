package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc
import org.postgresql.PGConnection

abstract class PostgreSqlCommon
  extends DBMS
  with Setters
  with PgDateTimeFormatter
  with IntervalImplicits
  with ConnectionImplicits
  with Getters
  with Updaters {

  override def dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
  override def driverClassName = "org.postgresql.Driver"
  override def jdbcSchemes = Set("postgresql")
  override def productName: String = "PostgreSQL"
  override val supportsIsValid: Boolean = true

  /**
   * Perform any connection initialization that should be done when a connection
   * is created. EG add a type mapping.
   *
   * By default this method does nothing.
   * @param connection
   */
  override def initializeConnection(connection: java.sql.Connection): Unit = {
    connection.unwrap[PGConnection](classOf[PGConnection]).addDataType("ltree", classOf[jdbc.LTree])
  }

}
