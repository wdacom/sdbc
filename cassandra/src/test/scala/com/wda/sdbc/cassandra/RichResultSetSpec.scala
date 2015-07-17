package com.wda.sdbc.cassandra

import com.wda.sdbc.Cassandra._

class RichResultSetSpec
  extends CassandraSuite {

  test("iterator() works on a single result") {implicit connection =>
    val results = Select[Int]("SELECT 1").iterator().toSeq
    assertResult(Seq(1))(results)
  }

  test("iterator() works on several results") {implicit connection =>
    val randoms = Seq.fill(10)(util.Random.nextInt())
    Select[Int]("CREATE TABLE tbl (x int)").execute()

    val insert = Select[Int]("INSERT INTO tbl (x) VALUES ($x)")

    for (random <- randoms) {
      insert.on("x" -> random).execute()
    }

    val results = Select[Int]("SELECT x FROM tbl").iterator()

    assertResult(randoms.toSet)(results.toSet)
  }

}
