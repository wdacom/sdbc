package com.rocketfuel.sdbc.h2.jdbc

import scalaz.Scalaz._

class SeqGetterSpec extends H2Suite {

  testSelect[Seq[Int]]("SELECT (1, 2, 3)", Seq(1, 2, 3).some)

  testSelect[Seq[Option[Int]]]("SELECT (1, NULL, 3)", Seq(1.some, none[Int], 3.some).some)

  testSelect[Seq[Seq[Int]]]("SELECT (())", Seq.empty.some)

}
