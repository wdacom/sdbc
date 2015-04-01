package com.wda.sdbc
package postgresql

import org.postgresql.PGConnection

trait ConnectionImplicits {

  /** Since a PostgreSql.Connection is guaranteed to be a PostgreSql Connection,
    * there is no danger in getting its underlying PGConnection value.
    * This can be used to get to the getCopyApi() and other methods.
    * @param connection The Connection or Hikari Connection which contains an underlying PGConnection.
    * @return The underlying PGConnection.
    */
  implicit def PostgreSqlConnectionToPGConnection(connection: PostgreSql.Connection): PGConnection = {
    connection.unwrap(classOf[PGConnection])
  }

}
