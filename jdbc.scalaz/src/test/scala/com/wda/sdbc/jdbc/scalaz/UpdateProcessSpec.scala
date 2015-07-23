package com.wda.sdbc.jdbc.scalaz

import scala.concurrent.duration._

class UpdateProcessSpec
  extends ProcessSuite {

  test("Use a stream of Updates to insert rows using a connection.") { implicit connection =>
    val insertCount = inserts.through(connection.updates).runLog.runFor(5.seconds).sum

    assertResult(expectedCount.toLong, "The expected number of rows were not inserted.")(insertCount)

  }

  test("Use a stream of Updates to insert rows using a connection pool.") { implicit connection =>

    val insertCount = inserts.through(pool.updates).runLog.runFor(5.seconds).sum

    assertResult(expectedCount, "The expected number of rows were not inserted.")(insertCount)

  }

}
