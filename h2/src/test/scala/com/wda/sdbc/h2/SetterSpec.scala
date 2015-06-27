package com.wda.sdbc.h2

import java.sql.Timestamp

import com.wda.sdbc.H2._
import org.joda.time.{LocalDateTime, Instant}
import org.scalatest.BeforeAndAfterEach

class SetterSpec
  extends H2Suite
  with BeforeAndAfterEach {

  test("Inserting and selecting an Instant works.") {implicit connection =>
    val now = Instant.now()
    Update("CREATE TABLE tbl (t timestamp)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> now).execute()
    connection.commit()
    connection.commit()
    val asTimestamp = Select[Timestamp]("SELECT t FROM tbl").single()
    val asDateTime = Select[Instant]("SELECT t FROM tbl").single()
    assertResult(now.getMillis)(asTimestamp.getTime)
    assertResult(now.getMillis)(asDateTime.getMillis)
  }

  test("Inserting and selecting a LocalDateTime works.") {implicit connection =>
    val now = LocalDateTime.now()
    Update("CREATE TABLE tbl (t timestamp)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> now).execute()
    connection.commit()
    val asLocalDateTime = Select[LocalDateTime]("SELECT t FROM tbl").single()
    assertResult(now)(asLocalDateTime)
  }

  override protected def beforeEach(): Unit = {
    withMemConnection() { implicit connection =>
      connection.execute("DROP TABLE IF EXISTS tbl")
      connection.commit()
    }
  }

}
