package com.wda.sdbc.sqlserver

import java.sql.Timestamp
import java.util.UUID

import com.wda.sdbc.SqlServer._
import org.joda.time.{Duration, LocalDateTime, Instant, DateTime}
import org.scalatest.BeforeAndAfterEach

class SetterSpec
  extends SqlServerSuite
  with BeforeAndAfterEach {

  test("Inserting and selecting a DateTime works.") {implicit connection =>
    val now = DateTime.now()
    Update("CREATE TABLE tbl (t datetimeoffset NOT NULL)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> now).execute()
    connection.commit()
    val asTimestamp = Select[Timestamp]("SELECT t FROM tbl").single()
    val asDateTime = Select[DateTime]("SELECT t FROM tbl").single()
    assertResult(now.getMillis)(asTimestamp.getTime)
    assertResult(now.getMillis)(asDateTime.getMillis)
  }

  test("Inserting and selecting an Instant works.") {implicit connection =>
    val now = Instant.now()
    Update("CREATE TABLE tbl (t datetime NOT NULL)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> now).execute()
    connection.commit()
    val asTimestamp = Select[Timestamp]("SELECT t FROM tbl").single()
    val asDateTime = Select[Instant]("SELECT t FROM tbl").single()
    assert((now.getMillis - asTimestamp.getTime).abs < 4)
    assert((now.getMillis - asDateTime.getMillis).abs < 4)
  }

  test("Inserting and selecting a LocalDateTime works.") {implicit connection =>
    val now = LocalDateTime.now()
    Update("CREATE TABLE tbl (t datetime NOT NULL)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> now).execute()
    connection.commit()
    val asLocalDateTime = Select[LocalDateTime]("SELECT t FROM tbl").single()
    //allow for some rounding, because datetime's resolution is > 1 ms
    val difference = new Duration(now.toDateTime, asLocalDateTime.toDateTime).getMillis
    assert(difference.abs < 4)
  }

  test("Inserting and selecting a UUID works.") {implicit connection =>
    val id = UUID.randomUUID()
    Update("CREATE TABLE tbl (t uniqueidentifier NOT NULL)").execute()
    Update("INSERT INTO tbl (t) VALUES ($v)").on("v" -> id).execute()
    connection.commit()
    val asUUID = Select[UUID]("SELECT t FROM tbl").single()
    assertResult(id)(asUUID)
  }

  override protected def beforeEach(): Unit = {
    withSql { implicit connection =>
      connection.execute("IF object_id('dbo.tbl') IS NOT NULL DROP TABLE tbl")
      connection.commit()
    }
  }

}
