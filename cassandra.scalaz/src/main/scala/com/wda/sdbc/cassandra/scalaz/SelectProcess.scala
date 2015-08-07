package com.wda.sdbc.cassandra.scalaz

import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator

import com.datastax.driver.core.Cluster
import com.wda.sdbc.{Cassandra, cassandra}
import com.wda.sdbc.Cassandra._
import scalaz._
import scalaz.concurrent.Task
import scalaz.stream._
import com.rocketfuel.scalaz.stream._
import com.google.common.util.concurrent.{FutureCallback, Futures}

object SelectProcess
  extends ProcessMethods {

  /**
   * Create a Process[T] from a Select.
   *
   * The Pool (i.e. Session) is not closed after the Process completes.
   * For resource safety. use forPool.
   * @param select
   * @param pool
   * @tparam T
   * @return
   */
  def forSelect[T](select: Select[T])(implicit pool: Pool): Process[Task, T] = {
    Process.iterator[T](runBoundStatement(cassandra.prepare(select)).map(_.map(select.converter)))
  }

  /**
   * Using a pool, create a Process that takes Selects and produces a Process[T] for each.
   * The pool will not be closed when the Process completes. If you want resource safety,
   * use forCluster.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   * @param pool
   * @tparam T
   * @return
   */
  def forPool[T](implicit pool: Pool): Channel[Task, Select[T], Process[Task, T]] = {
    channel.lift[Task, Select[T], Process[Task, T]] { select =>
      for {
        statement <- Task.delay(cassandra.prepare(select))
      } yield {
        val iteratorCreator = runBoundStatement(statement)
        Process.iterator(iteratorCreator).map(select.converter)
      }
    }
  }

  /**
   * Using a Cluster, create Pool (i.e. Session) to keySpace and create
   * a Process that takes Selects and produces a Process[T] for each. The
   * Pool will be closed after the Process completes.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   * @param keySpace
   * @param cluster
   * @tparam T
   * @return
   */
  def forCluster[T](keySpace: String)(implicit cluster: Cluster): Channel[Task, Select[T], Process[Task, T]] = {
    Process.await(Task(cluster.connect(keySpace))) { pool =>
      forPool[T](pool).onComplete(Process.eval_(closePool(pool)))
    }
  }

  /**
   * Using a Cluster, create Pool (i.e. Session) with no keyspace and create
   * a Process that takes Selects and produces a Process[T] for each. The
   * Pool will be closed after the Process completes.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   * @param cluster
   * @tparam T
   * @return
   */
  def forCluster[T](implicit cluster: Cluster): Channel[Task, Select[T], Process[Task, T]] = {
    Process.await(Task.delay(cluster.connect())) { pool =>
      forPool[T](pool).onComplete(Process.eval_(closePool(pool)))
    }
  }

  /**
   * Using a Cluster, create a Process that takes pairs of (keySpace, Select) and produces a Process[T] for each.
   * At most one pool is created for each keyspace. The Pools will be closed after the Process completes.
   *
   * Use merge.mergeN on the result to interleave the results, or .flatMap(identity)
   * to receive them in order.
   * @param cluster
   * @tparam T
   * @return
   */
  def forClusterWithKeyspace[T](implicit cluster: Cluster): Channel[Task, (String, Select[T]), Process[Task, T]] = {
    forClusterWithKeyspaceAux[Select[T], Process[Task, T]] {
      select => implicit pool =>
        Task.delay(Process.iterator(Task.delay(select.iterator())))
    }
  }

}
