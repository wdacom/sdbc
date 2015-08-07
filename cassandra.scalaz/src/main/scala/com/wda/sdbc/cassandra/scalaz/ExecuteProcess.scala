package com.wda.sdbc.cassandra.scalaz

import com.datastax.driver.core.Cluster

import scalaz.stream._
import com.wda.sdbc.Cassandra._
import com.wda.sdbc.cassandra

import scalaz.concurrent.Task

object ExecuteProcess
  extends ProcessMethods {

  /**
   * Create a Process[Unit] having one value from an Execute.
   *
   * The Pool (i.e. Session) is not closed after the Process completes.
   * For resource safety. use forCluster.
   * @param execute
   * @param pool
   * @return
   */
  def forExecute(execute: Execute)(implicit pool: Pool): Process[Task, Unit] = {
    Process.eval(ignoreBoundStatement(cassandra.prepare(execute)))
  }

  /**
   * Using a pool, create a Process that takes Executes and produces a Process.
   * The pool will not be closed when the Process completes. If you want resource safety,
   * use forCluster.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   * @param pool
   * @return
   */
  def forPool(implicit pool: Pool): Sink[Task, Execute] = {
    sink.lift[Task, Execute] { execute =>
      for {
        statement <- Task.delay(cassandra.prepare(execute))
        _ <- ignoreBoundStatement(statement)
      } yield ()
    }
  }


  /**
   * Using a Cluster, create Pool (i.e. Session) to keySpace and create
   * a Sink that takes Executes. The Pool will be closed after the Process completes.
   *
   * @param keySpace
   * @param cluster

   * @return
   */
  def forCluster(keySpace: String)(implicit cluster: Cluster): Sink[Task, Execute] = {
    Process.await(Task(cluster.connect(keySpace))) { pool =>
      forPool(pool).onComplete(Process.eval_(closePool(pool)))
    }
  }

  /**
   * Using a Cluster, create Pool (i.e. Session) with no keyspace and create
   * a Sink that takes Executes. The Pool will be closed after the Process completes.
   *
   * @param cluster
   * @return
   */
  def forCluster(implicit cluster: Cluster): Sink[Task, Execute] = {
    Process.await(Task(cluster.connect())) { pool =>
      forPool(pool).onComplete(Process.eval_(closePool(pool)))
    }
  }

  /**
   * Using a Cluster, create a Sink that takes pairs of (keySpace, Execute).
   * At most one pool is created for each keyspace. The Pools will be closed after the Process completes.
   *
   * @param cluster
   * @return
   */
  def forClusterWithKeyspace(implicit cluster: Cluster): Sink[Task, (String, Execute)] = {
    forClusterWithKeyspaceAux[Execute, Unit] {
      execute => implicit pool =>
        Task.delay(execute.execute())
    }
  }

}
