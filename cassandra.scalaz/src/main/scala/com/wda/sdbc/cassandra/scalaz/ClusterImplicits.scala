package com.wda.sdbc.cassandra.scalaz

import com.datastax.driver.core.Cluster
import com.wda.sdbc.Cassandra._

import scalaz.concurrent.Task
import scalaz.stream._

trait ClusterImplicits {

  implicit class ClusterChannels(cluster: Cluster) {
    def process[T]: Channel[Task, Select[T], Process[Task, T]] = {
      SelectProcess.forCluster(cluster)
    }
  }

}
