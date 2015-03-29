package com.wda.sdbc.sqlserver

import java.util.UUID

import com.wda.sdbc.SqlServer._

class RoundTripSpec
  extends SqlServerSuite {

  test("UUID survives a round trip") { implicit connection =>
    val uuid = UUID.randomUUID()
    val selected =
      Select[UUID]("SELECT CAST($uuid AS uniqueidentifier)").on(
        "uuid" -> uuid
      ).single()

    assertResult(uuid)(selected)
  }

  test("UUID survives a round trip as a string") { implicit connection =>
    val uuid = UUID.randomUUID()
    val selected =
      Select[UUID]("SELECT $uuid").on(
        "uuid" -> uuid
      ).single()

    assertResult(uuid)(selected)
  }

}
