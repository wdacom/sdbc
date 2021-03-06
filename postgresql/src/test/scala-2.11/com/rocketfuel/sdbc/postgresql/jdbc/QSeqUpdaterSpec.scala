package com.rocketfuel.sdbc.postgresql.jdbc

class QSeqUpdaterSpec
  extends PostgreSqlSuite {

  test("Updating an int[] works") {implicit connection =>
    Update("CREATE TABLE tbl (id serial PRIMARY KEY, ints int[])").update()

    Update("INSERT INTO tbl (ints) VALUES (@ints)").on("ints" -> QSeqUpdaterSpec.original).update()

    SelectForUpdate("SELECT id, ints FROM tbl").iterator().foreach {
      row =>
        row("ints") = QSeqUpdaterSpec.updated
        row.updateRow()
    }

    val selected = Select[Seq[Option[Int]]]("SELECT ints FROM tbl").iterator().toSeq

    assertResult(Seq(QSeqUpdaterSpec.updated))(selected)

  }

}

object QSeqUpdaterSpec {
  val original = Seq(1,2,3)

  val updated = Seq(None, Some(2), Some(3))
}
