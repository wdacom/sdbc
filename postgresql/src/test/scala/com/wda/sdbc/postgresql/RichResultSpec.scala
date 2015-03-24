package com.wda.sdbc.postgresql

import com.wda.sdbc.PostgreSql._

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

import scala.collection.immutable.Seq

class RichResultSpec
  extends PostgreSqlSuite
  with BeforeAndAfterEach
  with BeforeAndAfterAll {

  test("seq() works on a single result") {implicit connection =>
    val results = Select[Int]("SELECT 1::integer").seq()
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

    Update("CREATE TABLE tbl (id serial PRIMARY KEY, x int)").execute()

    val incrementedRandoms = randoms.map(_+1)

    val batch = randoms.foldRight(Batch("INSERT INTO tbl (x) VALUES ($x)")) {
      case (r, batch) =>
        batch.addBatch("x" -> r)
    }

    batch.executeBatch()

    for(row <- connection.iteratorForUpdate("SELECT * FROM tbl")) {
      row("x") = row[Int]("x") + 1
    }

    val incrementedFromDb = connection.seq[Int]("SELECT x FROM tbl ORDER BY x ASC")

    assert(incrementedFromDb.zip(incrementedRandoms).forall(xs => xs._1 == xs._2))
  }

  override protected def afterEach(): Unit = {
    withPg(_.execute("DROP TABLE IF EXISTS tbl;"))
  }

  override protected def beforeAll(): Unit = {
    pgBeforeAll()
  }

  override protected def afterAll(): Unit = {
    pgAfterAll()
  }

}
