package com.wda.sdbc.jdbc.scalaz.params.implicits

import com.wda.sdbc.jdbc._

import me.jeffshaw.scalaz.stream.IteratorConstructors._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait ConnectionMethods {
  self: PoolMethods =>

  implicit class ConnectionMethods(connection: Connection) {
    def batches(batch: Batch): Channel[Task, ParameterList, Seq[Long]] = {
      channel.lift { params =>
        Task.delay(batch.on(params: _*).seq()(connection))
      }
    }

    def executes(execute: Execute): Sink[Task, ParameterList] = {
      sink.lift { params =>
        Task.delay(execute.on(params: _*).execute()(connection))
      }
    }

    def selects[Value](select: Select[Value]): Channel[Task, ParameterList, Process[Task, Value]] = {
      channel.lift { params =>
        Task.delay(Process.iterator(Task.delay(select.on(params: _*).iterator()(connection))))
      }
    }

    def updates(update: Update): Channel[Task, ParameterList, Long] = {
      channel.lift { params =>
        Task.delay(update.on(params: _*).update()(connection))
      }
    }

  }

}
