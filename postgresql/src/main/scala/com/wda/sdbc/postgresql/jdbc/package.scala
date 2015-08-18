package com.wda.sdbc.postgresql

/**
 * Import the contents of this package to interact with [[http://www.postgresql.org/ PostgreSQL]] using JDBC.
 *
 * {{{
 * import com.wda.sdbc.postgresql.jdbc._
 *
 * val pool = Pool(...)
 *
 * pool.withConnection(_.iterator[Int]("SELECT 1").toSeq)
 * }}}
 */
package object jdbc extends implementation.PostgreSql {

}
