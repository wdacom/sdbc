package com.wda.sdbc.jdbc.scalaz

import scalaz.stream._
import scala.concurrent.duration._

class SelectProcessSpec
  extends ProcessSuite {

  test("Use a stream of Select to select rows using a connection.") { implicit connection =>
    val selectFuture = for {
      _ <- inserts.through(pool.updateProcess).run
      rows <- merge.mergeN(Process(select).through(connection.selectProcess)).runLog
    } yield rows

    val selectResults = selectFuture.runFor(5.seconds)

    assertResult(expectedCount)(selectResults.size.toLong)

    assertResult(insertSet)(selectResults.toSet)
  }

  test("Use a stream of Select to select rows using a connection pool.") { implicit connection =>

    val selectFuture = for {
      _ <- inserts.through(pool.updateProcess).run
      rows <- merge.mergeN(Process(select).through(pool.selectProcess)).runLog
    } yield rows

    val selectResults = selectFuture.runFor(5.seconds)

    assertResult(expectedCount)(selectResults.size.toLong)

    assertResult(insertSet)(selectResults.toSet)

  }

}
