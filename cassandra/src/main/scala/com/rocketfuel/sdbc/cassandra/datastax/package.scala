package com.rocketfuel.sdbc.cassandra

/**
 * Import the contents of this package to interact with [[http://cassandra.apache.org/ Apache Cassandra]] using the Datastax driver.
 *
 * {{{
 * import com.rocketfuel.sdbc.cassandra.datastax._
 *
 * val session = cluster.connect()
 *
 * session.iterator[Int]("SELECT 1")
 * }}}
 */
package object datastax extends implementation.Cassandra {

}
