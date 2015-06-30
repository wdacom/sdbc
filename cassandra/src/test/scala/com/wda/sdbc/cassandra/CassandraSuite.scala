package com.wda.sdbc.cassandra

import com.datastax.driver.core.{Cluster, Session}
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.{BeforeAndAfterEach, fixture, Outcome, BeforeAndAfterAll}

class CassandraSuite
  extends fixture.FunSuite
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  override protected def beforeAll(): Unit = {
    EmbeddedCassandraServerHelper.startEmbeddedCassandra()
  }

  override protected def beforeEach(): Unit = {
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
  }

  lazy val client = Cluster.builder().addContactPoint("127.0.0.1").build()

  override type FixtureParam = Session

  override protected def withFixture(test: OneArgTest): Outcome = {
    val session = client.connect()
    try {
      test.toNoArgTest(session)()
    } finally {
      session.close()
    }
  }

}
