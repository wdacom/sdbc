package com.rocketfuel.sdbc.sqlserver

/**
 * Import the contents of this package to interact with [[http://www.microsoft.com/en-us/server-cloud/products/sql-server/ Microsoft SQL Server]] using JDBC.
 *
 * {{{
 * import com.rocketfuel.sdbc.sqlserver.jdbc._
 *
 * val pool = Pool(...)
 *
 * pool.withConnection(_.iterator[Int]("SELECT 1").toSeq)
 * }}}
 */
package object jdbc extends implementation.SqlServer
