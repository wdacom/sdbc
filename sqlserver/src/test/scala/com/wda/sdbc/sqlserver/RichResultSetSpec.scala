package com.wda.sdbc.sqlserver

import com.wda.sdbc.SqlServer._

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import scala.collection.immutable.Seq

class RichResultSetSpec
  extends SqlServerSuite
  with BeforeAndAfterEach {

  test("seq() works on a single result") {implicit connection =>
    val results = Select[Int]("SELECT CAST(1 AS int)").iterator().toSeq
    assert(results == Seq(1))
  }

  test("seq() works on several results") {implicit connection =>
    val randoms = Seq.fill(10)(util.Random.nextInt())
    Update("CREATE TABLE tbl (x int)").execute()

    val batch = randoms.foldLeft(Batch("INSERT INTO tbl (x) VALUES ($x)")) {
      case (batch, r) =>
        batch.addBatch("x" -> r)
    }

    val insertions = batch.iterator()

    assert(insertions.sum == randoms.size)

    val results = Select[Int]("SELECT x FROM tbl").iterator().toSeq
    assert(results == randoms)
  }

  test("using SelectForUpdate to update a value works") {implicit connection =>
    val randoms = Seq.fill(10)(util.Random.nextInt()).sorted

    Update("CREATE TABLE tbl (id int IDENTITY(1,1) PRIMARY KEY, x int)").execute()

    val batch = randoms.foldLeft(Batch("INSERT INTO tbl (x) VALUES ($x)")) {
      case (batch, r) =>
        batch.addBatch("x" -> r)
    }

    batch.iterator()

    for (row <- connection.iteratorForUpdate("SELECT x FROM tbl")) {
      row("x") = row[Int]("x").map(_ + 1)
    }

    assert(connection.iterator[Int]("SELECT x FROM tbl ORDER BY x ASC").zip(randoms.iterator).forall{case (incremented, original) => incremented == original + 1})
  }

  override protected def afterEach(): Unit = {
    withSql(_.execute("IF object_id('dbo.tbl') IS NOT NULL DROP TABLE tbl"))
  }
}
