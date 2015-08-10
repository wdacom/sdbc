package com.wda.sdbc.cassandra.scalaz

import com.wda.sdbc.cassandra._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

object CassandraProcess {

  object cassandra {

    def execute(execute: Execute)(implicit session: Session): Process[Task, Unit] = {
      Process.eval(scalaz.runExecute(execute))
    }

    def select[T](select: Select[T])(implicit session: Session): Task[Process[Task, T]] = {
      scalaz.runSelect(select)
    }

    object params {
      def execute(session: Session, execute: Execute): Sink[Task, ParameterList] = {
        sink.lift[Task, Seq[(String, Option[ParameterValue[_]])]] { params =>
          runExecute(execute.on(params: _*))(session)
        }
      }

      def select[Value](session: Session, select: Select[Value]): Channel[Task, ParameterList, Process[Task, Value]] = {
        channel.lift[Task, Seq[(String, Option[ParameterValue[_]])], Process[Task, Value]] { params =>
          runSelect[Value](select.on(params: _*))(session)
        }
      }

      def execute(cluster: Cluster, execute: Execute, keyspace: Option[String] = None): Sink[Task, ParameterList] = {
        Process.await(connect(cluster, keyspace)) {implicit session =>
          params.execute(session, execute).onComplete(Process.eval_(closeSession(session)))
        }
      }

      def executeWithKeyspace[Value](cluster: Cluster, execute: Execute): Sink[Task, (String, ParameterList)] = {
        forClusterWithKeyspaceAux[ParameterList, Unit] { params => implicit pool =>
          runExecute(execute.on(params: _*))
        }(cluster)
      }

      def select[Value](cluster: Cluster, select: Select[Value], keyspace: Option[String] = None): Channel[Task, ParameterList, Process[Task, Value]] = {
        Process.await(connect(cluster, keyspace)) {implicit session =>
          params.select[Value](session, select).onComplete(Process.eval_(closeSession(session)))
        }
      }

      def selectWithKeyspace[Value](cluster: Cluster, select: Select[Value]): Channel[Task, (String, ParameterList), Process[Task, Value]] = {
        forClusterWithKeyspaceAux[ParameterList, Process[Task, Value]] { params => implicit pool =>
          runSelect[Value](select.on(params: _*))
        }(cluster)
      }
    }

    object keys {
      def execute[Key](session: Session)(implicit executable: Executable[Key]): Sink[Task, Key] = {
        sink.lift[Task, Key] { key =>
          runExecute(executable.execute(key))(session)
        }
      }

      def select[Key, Value](session: Session)(implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
        channel.lift[Task, Key, Process[Task, Value]] { key =>
          runSelect[Value](selectable.select(key))(session)
        }
      }

      def execute[Key](cluster: Cluster, keyspace: Option[String] = None)(implicit executable: Executable[Key]): Sink[Task, Key] = {
        Process.await(connect(cluster, keyspace)) { session =>
          execute(session).onComplete(Process.eval_(closeSession(session)))
        }
      }

      def executeWithKeyspace[Key, Value](cluster: Cluster)(implicit executable: Executable[Key]): Sink[Task, (String, Key)] = {
        forClusterWithKeyspaceAux[Key, Unit] { key => implicit session =>
          runExecute(executable.execute(key))
        }(cluster)
      }

      def selects[Key, Value](cluster: Cluster, keyspace: Option[String] = None)(implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
        Process.await(connect(cluster, keyspace)) { session =>
          select(session).onComplete(Process.eval_(closeSession(session)))
        }
      }

      def selectsWithKeyspace[Key, Value](cluster: Cluster)(implicit selectable: Selectable[Key, Value]): Channel[Task, (String, Key), Process[Task, Value]] = {
        forClusterWithKeyspaceAux[Key, Process[Task, Value]] { key => implicit session =>
          runSelect[Value](selectable.select(key))
        }(cluster)
      }
    }

  }

}
