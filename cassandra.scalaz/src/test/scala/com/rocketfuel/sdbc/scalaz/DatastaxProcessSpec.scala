package com.rocketfuel.sdbc.scalaz

import com.rocketfuel.sdbc.cassandra.datastax._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import datastax._
import scalaz.concurrent.Task
import scalaz.stream._

class DatastaxProcessSpec
  extends DatastaxSuite
  with GeneratorDrivenPropertyChecks {

  override implicit val generatorDrivenConfig: PropertyCheckConfig = PropertyCheckConfig(maxSize = 10)

  test("values are inserted and selected") {implicit connection =>
    Execute("CREATE KEYSPACE spc WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1}").execute()
    Execute("CREATE TABLE spc.tbl (x int PRIMARY KEY)").execute()

    forAll { (randoms: Seq[Int]) =>

      val insert: Process[Task, Unit] = {
        val execute = Execute("INSERT INTO spc.tbl (x) VALUES (@x)")
        val randomsParameters = Process.emitAll(randoms).map[ParameterList](x => Seq("x" -> x))
        randomsParameters.to(Process.datastax.params.execute(execute))
      }

      val select = Process.datastax.select(Select[Int]("SELECT x FROM spc.tbl"))

      val combined = for {
        _ <- insert
        ints <- select
      } yield ints

      val results = combined.runLog.run

      assertResult(randoms.sorted)(results.sorted)

      RichResultSetSpec.truncate.execute()
    }
  }

}
