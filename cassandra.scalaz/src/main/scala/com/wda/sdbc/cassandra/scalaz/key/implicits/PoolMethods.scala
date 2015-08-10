package com.wda.sdbc.cassandra.scalaz.key.implicits

import com.wda.sdbc.cassandra
import com.wda.sdbc.cassandra.scalaz._

import scalaz.concurrent.Task
import scalaz.stream._

trait PoolMethods {
  implicit class PoolMethods(pool: cassandra.Session) {

    def executes[Key](implicit executable: cassandra.Executable[Key]): Sink[Task, Key] = {
      sink.lift[Task, Key] { key =>
        runExecute(executable.execute(key))(pool)
      }
    }

    def selects[Key, Value](implicit selectable: cassandra.Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
      channel.lift[Task, Key, Process[Task, Value]] { key =>
        runSelect[Value](selectable.select(key))(pool)
      }
    }
  }
}
