package com.wda.sdbc.cassandra

/**
 * Import the contents of this package to interact with [[http://cassandra.apache.org/ Apache Cassandra]] using the Datastax driver.
 *
 * {{{
 * import com.wda.sdbc.cassandra.datastax._
 *
 * val session = cluster.connect()
 *
 * session.iterator[Int]("SELECT 1")
 * }}}
 */
package object datastax extends implementation.Cassandra {

}
