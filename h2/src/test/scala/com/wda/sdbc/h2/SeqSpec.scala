package com.wda.sdbc.h2

import com.wda.sdbc.H2._

class SeqSpec
  extends H2Suite {

  val s = Seq(BigDecimal("3.14159"))

  test("Seq[BigDecimal] survives round trip") { implicit connection =>
    Update("CREATE TABLE tbl (a array)").execute()

    Update("INSERT INTO tbl (a) VALUES ($array)").on(
      "array" -> s
    ).execute()

    val result = Select[Seq[BigDecimal]]("SELECT a FROM tbl").single()

    assertResult(s)(result)
  }

}
