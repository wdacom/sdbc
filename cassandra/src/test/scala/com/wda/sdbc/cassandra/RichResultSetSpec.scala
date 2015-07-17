package com.wda.sdbc.cassandra

import com.wda.sdbc.Cassandra._

class RichResultSetSpec
  extends CassandraSuite {

  test("iterator() works on several results") {implicit connection =>
    Select[Unit]("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()

    val randoms = Seq.fill(10)(util.Random.nextInt())
    Select[Int]("CREATE TABLE spc.tbl (x int PRIMARY KEY)").execute()

    val insert = Select[Int]("INSERT INTO spc.tbl (x) VALUES ($x)")

    for (random <- randoms) {
      insert.on("x" -> random).execute()
    }

    val results = Select[Int]("SELECT x FROM spc.tbl").iterator()

    assertResult(randoms.toSet)(results.toSet)
  }

}
