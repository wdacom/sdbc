package com.wda.sdbc.h2

import com.wda.sdbc.H2._

class SerializableSpec
  extends H2Suite {

  test("Serializable value survives round trip") { implicit connection =>

    val v = Serialize(util.Success(BigDecimal("3.14159")))

    Update("CREATE TABLE tbl (obj other)").execute()

    Update("INSERT INTO tbl (obj) VALUES ($obj)").on(
      "obj" -> v
    ).execute()

    val result = Select[Deserialize[util.Try[BigDecimal]]]("SELECT obj FROM tbl").single()

    assertResult(v)(result.value)

  }

}
