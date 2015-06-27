package com.wda.sdbc.postgresql

import java.sql.Timestamp
import java.util.UUID

import com.wda.sdbc.PostgreSql._
import org.joda.time.{LocalDateTime, Instant, DateTime}
import org.scalatest.BeforeAndAfterEach

class SetterSpec
  extends PostgreSqlSuite
  with BeforeAndAfterEach {

  test("Inserting and selecting a DateTime works.") {implicit connection =>
    val now = DateTime.now()
    Update("CREATE TABLE tbl (t timestamp with time zone)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> now).execute()
    connection.commit()
    val asTimestamp = Select[Timestamp]("SELECT t FROM tbl").single()
    val asDateTime = Select[DateTime]("SELECT t FROM tbl").single()
    assertResult(now.getMillis)(asTimestamp.getTime)
    assertResult(now.getMillis)(asDateTime.getMillis)
  }

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

  test("Inserting and selecting a UUID works.") {implicit connection =>
    val id = UUID.randomUUID()
    Update("CREATE TABLE tbl (t uuid)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> id).execute()
    connection.commit()
    val asUUID = Select[UUID]("SELECT t FROM tbl").single()
    assertResult(id)(asUUID)
  }

  override protected def beforeEach(): Unit = {
    withPg { implicit connection =>
      connection.execute("DROP TABLE IF EXISTS tbl")
      connection.commit()
    }
  }

}
