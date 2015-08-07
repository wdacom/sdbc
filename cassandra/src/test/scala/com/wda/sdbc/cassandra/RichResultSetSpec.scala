package com.wda.sdbc.cassandra

import com.wda.sdbc.Cassandra._
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import scala.reflect._

class RichResultSetSpec
  extends CassandraSuite
  with GeneratorDrivenPropertyChecks {

  override implicit val generatorDrivenConfig: PropertyCheckConfig = PropertyCheckConfig(maxSize = 10)

  test("iterator() works on several results") {implicit connection =>
    Execute("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()
    Execute("CREATE TABLE spc.tbl (x int PRIMARY KEY)").execute()

    forAll { (randoms: Seq[Int]) =>
      val insert = Execute("INSERT INTO spc.tbl (x) VALUES ($x)")

      for (random <- randoms) {
        insert.on("x" -> random).execute()
      }

      val results = Select[Int]("SELECT x FROM spc.tbl").iterator()

      assertResult(randoms.toSet)(results.toSet)

      RichResultSetSpec.truncate.execute()
    }
  }

  test("Insert and select works for tuples.") { implicit connection =>
    Execute("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()
    Execute("CREATE TABLE spc.tbl (x tuple<int, int> PRIMARY KEY)").execute()

    forAll { (tuples: Seq[(Int, Int)]) =>
      //Note: Peng verified that values in tuples are nullable, so we need
      //to support that.

      val insert = Execute("INSERT INTO spc.tbl (x) VALUES ($x)")

      for (tuple <- tuples) {
        insert.on("x" -> tuple).execute()
      }

      val results = Select[(Option[Int], Option[Int])]("SELECT x FROM spc.tbl").iterator()

      val expectedResults = tuples.map { case (x, y) => (Some(x), Some(y)) }

      assertResult(expectedResults.toSet)(results.toSet)

      RichResultSetSpec.truncate.execute()
    }
  }

  test("Insert and select works for tuples having some null elements.") {implicit connection =>
    Execute("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()
    Execute("CREATE TABLE spc.tbl (x tuple<int, int> PRIMARY KEY)").execute()

    forAll { (tuples: Seq[(Option[Int], Option[Int])]) =>
      val insert = Execute("INSERT INTO spc.tbl (x) VALUES ($x)")

      for (tuple <- tuples) {
        insert.on("x" -> tuple).execute()
      }

      val results = Select[(Option[Int], Option[Int])]("SELECT x FROM spc.tbl").iterator()

      assertResult(tuples.toSet)(results.toSet)

      RichResultSetSpec.truncate.execute()
    }
  }

  test("Insert and select works for sets.") {implicit connection =>
    Execute("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()
    Execute("CREATE TABLE spc.tbl (id int PRIMARY KEY, x set<text>)").execute()

    forAll(Gen.nonEmptyListOf(Gen.nonEmptyContainerOf[Set, String](Gen.alphaStr))) { sets =>
      val insert = Execute("INSERT INTO spc.tbl (id, x) VALUES ($id, $x)")

      for ((set, id) <- sets.zipWithIndex) {
        insert.on( "id" -> id, "x" -> set).execute()
      }

      val results = Select[Set[String]]("SELECT x FROM spc.tbl").iterator()

      assertResult(sets.toSet)(results.toSet)

      RichResultSetSpec.truncate.execute()
    }
  }

  val genStringTuple = for {
    t0 <- Gen.alphaStr
    t1 <- Gen.alphaStr
  } yield (t0, t1)

  implicit val getter = MapRowGetter[String, String](classTag[String], classTag[String])

  test("Insert and select works for maps.") {implicit connection =>
    Execute("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()
    Execute("CREATE TABLE spc.tbl (id int PRIMARY KEY, x map<text, text>)").execute()

    forAll(Gen.nonEmptyListOf[Map[String, String]](Gen.nonEmptyMap[String, String](genStringTuple))) { maps =>
      val insert = Execute("INSERT INTO spc.tbl (id, x) VALUES ($id, $x)")

      for ((map, id) <- maps.zipWithIndex) {
        insert.on("id" -> id, "x" -> map).execute()
      }

      val results = Select[Map[String, String]]("SELECT x FROM spc.tbl").iterator()

      assertResult(maps.toSet)(results.toSet)

      RichResultSetSpec.truncate.execute()
    }
  }

}

object RichResultSetSpec {
  val truncate = Execute("TRUNCATE spc.tbl")
}
