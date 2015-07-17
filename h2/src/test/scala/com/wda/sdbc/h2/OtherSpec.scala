package com.wda.sdbc.h2

import com.wda.sdbc.H2._

class OtherSpec
  extends H2Suite {

  test("Serializable value survives round trip") { implicit connection =>

    val original = util.Success(BigDecimal("3.14159"))

    Update("CREATE TABLE tbl (obj other)").execute()

    Update("INSERT INTO tbl (obj) VALUES ($obj)").on(
      "obj" -> Serialized(original)
    ).execute()

    val result = Select[Serialized]("SELECT obj FROM tbl").option()

    assertResult(Some(original))(result.map(_.value))

  }

}
