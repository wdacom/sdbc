package com.wda.sdbc.cassandra.scalaz.key

import com.datastax.driver.core.Cluster
import com.wda.sdbc.Cassandra._
import com.wda.sdbc.cassandra
import com.wda.sdbc.cassandra.scalaz.{ExecuteImplicits, ProcessMethods}
import me.jeffshaw.scalaz.stream.IteratorConstructors._

import scalaz.concurrent.Task
import scalaz.stream._

trait ClusterMethods
  extends ProcessMethods {
  self: ExecuteImplicits =>

  implicit class ClusterMethods(cluster: Cluster) {

    private implicit val implicitCluster = cluster

    def execute[Key](implicit executable: Executable[Key]): Sink[Task, Key] = {
      Process.await(Task.delay(cluster.newSession())) {implicit pool =>
        sink.lift[Task, Key] { key =>
          ignoreBoundStatement(cassandra.prepare(executable.execute(key)))
        }.onComplete(Process.eval_(Task.delay(pool.close())))
      }
    }

    def executeWithKeyspace[Key](implicit executable: Executable[Key]): Sink[Task, (String, Key)] = {
      forClusterWithKeyspaceAux[Key, Unit] { key => implicit pool =>
        Task.delay(executable.execute(key).execute())
      }
    }

    def select[Key, Value](implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
      Process.await(Task.delay(cluster.newSession())) {implicit pool =>
        channel.lift[Task, Key, Process[Task, Value]] { key =>
          val select = selectable.select(key)
          val iteratorCreator = for {
            iterator <- runBoundStatement(cassandra.prepare(select))
          } yield {
            iterator.map(select.converter)
          }
          Task.delay(Process.iterator(iteratorCreator))
        }.onComplete(Process.eval_(Task.delay(pool.close())))
      }
    }
  }

}
