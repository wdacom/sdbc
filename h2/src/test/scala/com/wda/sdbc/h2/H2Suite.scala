package com.wda.sdbc.h2

import com.wda.sdbc.H2._
import org.scalatest._

abstract class H2Suite
  extends fixture.FunSuite {

  def testSelect[T](query: String, expectedValue: Option[T])(implicit converter: Row => Option[T]): Unit = {
    test(query) { implicit connection =>
      val result = Select[Option[T]](query).option().flatten
      (expectedValue, result) match {
        case (Some(expectedArray: Array[_]), Some(resultArray: Array[_])) =>
          assert(expectedArray.sameElements(resultArray))
        case (Some(x), Some(y)) =>
          assertResult(x)(y)
        case (None, None) => true
        case _ => false
      }
    }
  }

  type FixtureParam = Connection

  override protected def withFixture(test: OneArgTest): Outcome = {
    withMemConnection[Outcome](name = "test", dbCloseDelay = None) { connection: Connection =>
      withFixture(test.toNoArgTest(connection))
    }
  }
}
