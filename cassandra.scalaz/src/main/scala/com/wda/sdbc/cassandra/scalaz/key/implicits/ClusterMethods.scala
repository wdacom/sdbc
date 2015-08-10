package com.wda.sdbc.cassandra.scalaz.key.implicits

import com.wda.sdbc.cassandra._
import com.wda.sdbc.cassandra.scalaz._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait ClusterMethods {
  self: PoolMethods =>

  implicit class ClusterMethods(cluster: Cluster) {

    def executes[Key](keyspace: Option[String] = None)(implicit executable: Executable[Key]): Sink[Task, Key] = {
      Process.await(connect(cluster, keyspace)) { pool =>
        pool.executes[Key].onComplete(Process.eval_(closePool(pool)))
      }
    }

    def executesWithKeyspace[Key, Value]()(implicit executable: Executable[Key]): Sink[Task, (String, Key)] = {
      forClusterWithKeyspaceAux[Key, Unit] { key => implicit pool =>
        runExecute(executable.execute(key))
      }(cluster)
    }

    def selects[Key, Value](keyspace: Option[String] = None)(implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
      Process.await(connect(cluster, keyspace)) { pool =>
        pool.selects[Key, Value].onComplete(Process.eval_(closePool(pool)))
      }
    }

    def selectsWithKeyspace[Key, Value]()(implicit selectable: Selectable[Key, Value]): Channel[Task, (String, Key), Process[Task, Value]] = {
      forClusterWithKeyspaceAux[Key, Process[Task, Value]] { key => implicit pool =>
        runSelect[Value](selectable.select(key))
      }(cluster)
    }
  }

}
