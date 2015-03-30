package com.wda.sdbc.h2

import java.nio.file.Path
import java.sql.DriverManager

import com.wda.sdbc.DBMS
import com.wda.sdbc.base.{Java8DefaultGetters, DefaultSetters}

abstract class H2
  extends DBMS
  with Java8DefaultGetters
  with DefaultSetters
  with SerializedParameter
  with SerializedGetter
  with SerializedSetter {
  /**
   * Class name for the DataSource class.
   */
  override def dataSourceClassName: String = "org.h2.jdbcx.JdbcDataSource"

  /**
   * Class name for the JDBC driver class.
   */
  override def driverClassName: String = "org.h2.Driver"

  //http://www.h2database.com/html/cheatSheet.html
  override def jdbcSchemes: Set[String] = {
    Set(
      "h2",
      "h2:mem",
      "h2:tcp"
    )
  }

  /**
   * If the JDBC driver supports the .isValid() method.
   */
  override def supportsIsValid: Boolean = true

  override def Identifier: Identifier = new Identifier

  /**
   * The result of getMetaData.getDatabaseProductName
   */
  override def productName: String = "H2"

  def withMemConnection[T](name: String = "")(f: Connection => T): T = {
    val connection = DriverManager.getConnection("jdbc:h2:mem:" + name)
    try {
      f(connection)
    } finally {
      connection.closeQuietly()
    }
  }

  def withFileConnection[T](path: Path)(f: Connection => T): T = {
    val connection = DriverManager.getConnection("jdbc:h2:" + path.toFile.getCanonicalPath)

    try {
      f(connection)
    } finally {
      connection.closeQuietly()
    }
  }
}
