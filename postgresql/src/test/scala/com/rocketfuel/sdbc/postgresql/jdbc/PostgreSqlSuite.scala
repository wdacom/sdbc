package com.rocketfuel.sdbc.postgresql.jdbc

import com.typesafe.config.{ConfigFactory, Config}
import org.joda.time.DateTime
import com.rocketfuel.sdbc.config.TestingConfig
import org.scalatest._

abstract class PostgreSqlSuite
  extends fixture.FunSuite
  with HasPostgreSqlPool
  with TestingConfig
  with PgTestingConfig
  with BeforeAndAfterAll {

  override def config: Config = ConfigFactory.load("sql-testing.conf")

  override def pgConfigKey: String = "pg"

  def testSelect[T](query: String, expectedValue: Option[T])(implicit converter: Row => Option[T]): Unit = {
    test(query) { implicit connection =>
      val result = Select[Option[T]](query).option().get
      (expectedValue, result) match {
        case (Some(expectedArray: Array[Byte]), Some(resultArray: Array[Byte])) =>
          assert(expectedArray.sameElements(resultArray))
        case (Some(expectedOffset: DateTime), Some(resultOffset: DateTime)) =>
          assertResult(expectedOffset.toInstant)(resultOffset.toInstant)
        case (Some(x), Some(y)) =>
          assertResult(x)(y)
        case (None, None) => true
        case _ => false
      }
    }
  }

  type FixtureParam = Connection

  override protected def withFixture(test: OneArgTest): Outcome = {
    withPg[Outcome] { connection =>
      withFixture(test.toNoArgTest(connection))
    }
  }

  override protected def beforeAll(): Unit = {
    pgBeforeAll()
  }

  override protected def afterAll(): Unit = {
    pgAfterAll()
  }
}
