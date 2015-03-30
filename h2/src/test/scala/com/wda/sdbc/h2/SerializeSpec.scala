package com.wda.sdbc.h2

import com.wda.sdbc.H2._

class SerializeSpec
  extends H2Suite {

  test("Serializable value survives round trip") { implicit connection =>

    val v = util.Success(BigDecimal("3.14159"))

    Update("CREATE TABLE tbl (obj other)").execute()

    Update("INSERT INTO tbl (obj) VALUES ($obj)").on(
      "obj" -> Serialized(v)
    ).execute()

    val Serialized(result) = Select[Serialized]("SELECT obj FROM tbl")(GetterToRowSingleton[Serialized](SerializeGetter)).single()

    assertResult(v)(result)

  }

}
