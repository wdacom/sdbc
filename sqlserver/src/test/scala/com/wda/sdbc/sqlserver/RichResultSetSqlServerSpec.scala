package com.wda.sdbc.sqlserver

import com.wda.sdbc.SqlServer._

import org.scalatest.BeforeAndAfterEach

import scala.collection.immutable.Seq

class RichResultSetSqlServerSpec
  extends SqlServerSuite
  with BeforeAndAfterEach {

  test("seq() works on a single result") {implicit connection =>
    val results = Select[Int]("SELECT CAST(1 AS int)").seq()
    assert(results == Seq(1))
  }

  test("seq() works on several results") {implicit connection =>
    val randoms = Seq.fill(10)(util.Random.nextInt())
    Update("CREATE TABLE tbl (x int)").execute()

    val batch = randoms.foldLeft(Batch("INSERT INTO tbl (x) VALUES ($x)")) {
      case (batch, r) =>
        batch.addBatch("x" -> r)
    }

    val insertions = batch.executeBatch()

    assert(insertions.sum[Int] == randoms.size)

    val results = Select[Int]("SELECT x FROM tbl").seq()
    assert(results == randoms)
  }

  test("using SelectForUpdate to update a value works") {implicit connection =>
    val randoms = Seq.fill(10)(util.Random.nextInt()).sorted

    Update("CREATE TABLE tbl (id int IDENTITY(1,1) PRIMARY KEY, x int)").execute()

    val batch = randoms.foldLeft(Batch("INSERT INTO tbl (x) VALUES ($x)")) {
      case (batch, r) =>
        batch.addBatch("x" -> r)
    }

    batch.executeBatch()

    for(row <- connection.iteratorForUpdate("SELECT x FROM tbl")) {
      row("x") = row[Int]("x") + 1
    }

    assert(connection.seq[Int]("SELECT x FROM tbl ORDER BY x ASC").zip(randoms).forall{case (incremented, original) => incremented == original + 1})
  }

  override protected def afterEach(): Unit = {
    withSql(_.execute("IF object_id('dbo.tbl') IS NOT NULL DROP TABLE tbl"))
  }
}
