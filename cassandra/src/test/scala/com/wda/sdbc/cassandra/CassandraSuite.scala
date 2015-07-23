package com.wda.sdbc.cassandra

import com.datastax.driver.core.{Cluster, Session}
import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.scalatest.{BeforeAndAfterEach, fixture, Outcome, BeforeAndAfterAll}

abstract class CassandraSuite
  extends fixture.FunSuite
  with BeforeAndAfterAll
  with BeforeAndAfterEach {

  override protected def beforeAll(): Unit = {
    EmbeddedCassandraServerHelper.startEmbeddedCassandra("another-cassandra.yaml")
  }

  override protected def beforeEach(): Unit = {
    EmbeddedCassandraServerHelper.cleanEmbeddedCassandra()
  }

  override type FixtureParam = Session

  override protected def withFixture(test: OneArgTest): Outcome = {
    val client = Cluster.builder().addContactPoint("localhost").withPort(9142).build()

    val session = client.connect()
    try {
      test.toNoArgTest(session)()
    } finally {
      session.close()
    }
  }

}
