package com.wda.sdbc.cassandra.scalaz.params.implicits

import com.wda.sdbc.cassandra._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait ExecuteMethods {
  implicit class ExecuteMethods(execute: Execute) {
    def process()(implicit session: Session): Process[Task, Unit] = {
      Process.eval(scalaz.runExecute(execute))
    }

    def processes()(implicit session: Session): Channel[Task, ParameterList, Unit] = {
      channel.lift(params => scalaz.runExecute(execute.on(params: _*)))
    }
  }
}
