package com.wda.sdbc.cassandra.scalaz.key

import com.wda.sdbc.Cassandra._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

import scalaz.concurrent.Task
import scalaz.stream._

trait PoolMethods {

  trait PoolMethods {
    implicit class PoolMethods(val pool: Pool) {
      def executes[Key](implicit executable: Executable[Key]): Sink[Task, Key] = {
        sink.lift[Task, Key] { key =>
          Task.delay(execute(key)(executable, pool))
        }
      }

      def selects[Key, Value](implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
        channel.lift[Task, Key, Process[Task, Value]] { key =>
          Task.delay(Process.iterator(Task.delay(selectable.select(key).iterator()(pool))))
        }
      }
    }
  }
}
