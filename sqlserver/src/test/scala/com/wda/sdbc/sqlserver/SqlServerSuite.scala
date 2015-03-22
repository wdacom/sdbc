package com.wda.sdbc.sqlserver

import java.time.{Instant, OffsetDateTime}

import com.wda.sdbc.SqlServer._
import com.wda.sdbc.config.{HasSqlTestingConfig, TestingConfig}
import org.scalatest._

abstract class SqlServerSuite
  extends fixture.FunSuite
  with HasSqlServerPool
  with BeforeAndAfterAll
  with TestingConfig
  with HasSqlTestingConfig {

  def testSelect[T](query: String, expectedValue: Option[T])(implicit getter: Getter[T]): Unit = {
    test(query) { implicit connection =>
      val result = Select[Option[T]](query).single()
      (expectedValue, result) match {
        case (Some(expectedArray: Array[Byte]), Some(resultArray: Array[Byte])) =>
          assert(expectedArray.sameElements(resultArray))
        case (Some(expectedOffset: OffsetDateTime), Some(resultOffset: OffsetDateTime)) =>
          assertResult(expectedOffset.toInstant)(resultOffset.toInstant)
        case (Some(expectedInstant: Instant), Some(resultInstant: Instant)) =>
          assertResult(expectedInstant)(resultInstant)
        case (Some(x), Some(y)) =>
          assertResult(x)(y)
        case (None, None) => true
        case _ => false
      }
    }
  }

  type FixtureParam = Connection

  override protected def withFixture(test: OneArgTest): Outcome = {
    withSql[Outcome] { connection =>
      withFixture(test.toNoArgTest(connection))
    }
  }
}
