package com.wda.sdbc.cassandra.scalaz.params.implicits

import com.datastax.driver.core.Cluster
import com.wda.sdbc.cassandra._
import com.wda.sdbc.cassandra.scalaz._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait ClusterMethods {
  self: PoolMethods =>

  implicit class ClusterMethods(cluster: Cluster) {
    def executes(execute: Execute, keyspace: Option[String] = None): Sink[Task, ParameterList] = {
      Process.await(connect(cluster, keyspace)) {implicit pool =>
        pool.executes(execute).onComplete(Process.eval_(closePool(pool)))
      }
    }

    def executesWithKeyspace[Value](execute: Execute): Sink[Task, (String, ParameterList)] = {
      forClusterWithKeyspaceAux[ParameterList, Unit] { params => implicit pool =>
        runExecute(execute.on(params: _*))
      }(cluster)
    }

    def selects[Value](select: Select[Value], keyspace: Option[String] = None): Channel[Task, ParameterList, Process[Task, Value]] = {
      Process.await(connect(cluster, keyspace)) {implicit pool =>
        pool.selects(select).onComplete(Process.eval_(closePool(pool)))
      }
    }

    def selectsWithKeyspace[Value](select: Select[Value]): Channel[Task, (String, ParameterList), Process[Task, Value]] = {
      forClusterWithKeyspaceAux[ParameterList, Process[Task, Value]] { params => implicit pool =>
        runSelect[Value](select.on(params: _*))
      }(cluster)
    }
  }

}
