package com.wda.sdbc.jdbc.scalaz.key.implicits

import com.wda.sdbc.jdbc._
import me.jeffshaw.scalaz.stream.IteratorConstructors._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait ConnectionMethods {

  implicit class ConnectionMethods(connection: Connection) {
    def batches[Key](implicit batchable: Batchable[Key]): Channel[Task, Key, Process[Task, Long]] = {
      channel.lift[Task, Key, Process[Task, Long]] { key =>
        Task.delay(Process.iterator(Task.delay(batchable.batch(key).iterator()(connection))))
      }
    }

    def executes[Key](implicit executable: Executable[Key]): Sink[Task, Key] = {
      sink.lift[Task, Key] { key =>
        Task.delay(executable.execute(key).execute()(connection))
      }
    }

    def selects[Key, Value](implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
      channel.lift[Task, Key, Process[Task, Value]] { key =>
        Task.delay(Process.iterator(Task.delay(selectable.select(key).iterator()(connection))))
      }
    }

    def updates[Key](implicit updatable: Updatable[Key]): Channel[Task, Key, Long] = {
      channel.lift[Task, Key, Long] { key =>
        Task.delay(updatable.update(key).update()(connection))
      }
    }
  }

}
