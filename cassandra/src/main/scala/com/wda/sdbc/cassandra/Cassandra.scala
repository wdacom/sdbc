package com.wda.sdbc.cassandra

import com.datastax.driver.core.{ResultSet, Row}

abstract class Cassandra {



  val row: Row = ???

  val rs: ResultSet = ???


}
