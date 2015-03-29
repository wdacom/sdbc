package com.wda.sdbc.h2

import com.wda.sdbc.H2._

import java.io

class SeqSpec
  extends H2Suite {

  val s = Seq(BigDecimal("3.14159"))

  test("Seq[BigDecimal] survives round trip") { implicit connection =>
    val result = Select[Seq[io.Serializable]]("SELECT CAST($s AS array)").on("s" -> s).single()
    assertResult(s)(result)
  }

}
