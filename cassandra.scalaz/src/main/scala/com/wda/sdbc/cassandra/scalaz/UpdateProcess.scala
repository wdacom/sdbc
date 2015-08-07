package com.wda.sdbc.cassandra.scalaz

import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator

import com.datastax.driver.core.Cluster

import scalaz.stream._
import com.wda.sdbc.Cassandra._
import com.wda.sdbc.{Cassandra, cassandra}

import scalaz.concurrent.Task

object UpdateProcess
  extends ProcessMethods {

  /**
   * Create a Process[Unit] having one value from an Update.
   *
   * The Pool (i.e. Session) is not closed after the Process completes.
   * For resource safety. use forCluster.
   * @param update
   * @param pool
   * @return
   */
  def forUpdate(update: Update)(implicit pool: Pool): Process[Task, Unit] = {
    Process.eval(ignoreBoundStatement(cassandra.prepare(update)))
  }

  /**
   * Using a pool, create a Process that takes Updates and produces a Process.
   * The pool will not be closed when the Process completes. If you want resource safety,
   * use forCluster.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   * @param pool
   * @return
   */
  def forPool(implicit pool: Pool): Sink[Task, Update] = {
    sink.lift[Task, Update] { update =>
      for {
        statement <- Task.delay(cassandra.prepare(update))
        _ <- ignoreBoundStatement(statement)
      } yield ()
    }
  }


  /**
   * Using a Cluster, create Pool (i.e. Session) to keySpace and create
   * a Sink that takes Updates. The Pool will be closed after the Process completes.
   *
   * @param keySpace
   * @param cluster

   * @return
   */
  def forCluster(keySpace: String)(implicit cluster: Cluster): Sink[Task, Update] = {
    Process.await(Task(cluster.connect(keySpace))) { pool =>
      forPool(pool).onComplete(Process.eval_(closePool(pool)))
    }
  }

  /**
   * Using a Cluster, create Pool (i.e. Session) with no keyspace and create
   * a Sink that takes Updates. The Pool will be closed after the Process completes.
   *
   * @param cluster
   * @return
   */
  def forCluster(implicit cluster: Cluster): Sink[Task, Update] = {
    Process.await(Task(cluster.connect())) { pool =>
      forPool(pool).onComplete(Process.eval_(closePool(pool)))
    }
  }

  /**
   * Using a Cluster, create a Process that takes pairs of (keySpace, Select) and produces a Process[T] for each.
   * At most one pool is created for each keyspace. The Pools will be closed after the Process completes.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   *
   * @param cluster
   * @return
   */
  def forClusterWithKeyspace(implicit cluster: Cluster): Sink[Task, (String, Update)] = {
    val poolsRef = new AtomicReference(Map.empty[String, Pool])

    /**
     * Get a Pool for the keyspace, creating it if it does not exist.
     * @param keySpace
     * @return
     */
    def getPool(keySpace: String): Task[Pool] = Task.delay {
      val pools =
        poolsRef.updateAndGet(
          new UnaryOperator[Map[String, Pool]] {
            override def apply(t: Map[String, Cassandra.Pool]): Map[String, Cassandra.Pool] = {
              if (t.contains(keySpace)) t
              else {
                val pool = cluster.connect(keySpace)
                t + (keySpace -> pool)
              }
            }
          }
        )

      pools(keySpace)
    }

    /**
     * Empty the pools collection, and close all the pools.
     */
    val closePools: Task[Unit] = {
      val getToClose = Task.delay[Map[String, Pool]] {
        poolsRef.getAndUpdate(
          new UnaryOperator[Map[String, Pool]] {
            override def apply(t: Map[String, Cassandra.Pool]): Map[String, Cassandra.Pool] = {
              Map.empty
            }
          }
        )
      }

      for {
        toClose <- getToClose
        _ <- Task.gatherUnordered(toClose.map(kvp => closePool(kvp._2)).toSeq)
      } yield ()
    }

    sink.lift[Task, (String, Update)] { case (keyspace, update) =>
      for {
        pool <- getPool(keyspace)
        _ <- Task.delay(update.execute()(pool))
      } yield ()
    }.onComplete(Process.eval_(closePools))
  }

}
