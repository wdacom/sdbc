package com.rocketfuel.sdbc.h2.jdbc

class UpdatersSpec extends H2Suite {

  test("Update Int works") {implicit connection =>
    val id = 1

    Update("CREATE TABLE tbl (id int PRIMARY KEY)").update()

    update"INSERT INTO tbl (id) VALUES ($id)".update()

    for (row <- select"SELECT * FROM tbl".iterator()) {
      println(row.get[Int]("id"))
    }

    for (row <- selectForUpdate"SELECT * FROM tbl".iterator()) {
      row("id") = row.get[Int]("id").map(_ + 1)
      row.updateRow()
    }

    val ids = Select[Int]("SELECT id FROM tbl").option()

    assert(ids.nonEmpty)

    assertResult(Some(id + 1))(ids)
  }

}
