package com.wda.sdbc.h2

import com.wda.sdbc.DBMS

abstract class H2
  extends DBMS {
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

}
