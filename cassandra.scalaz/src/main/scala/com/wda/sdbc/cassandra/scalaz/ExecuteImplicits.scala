package com.wda.sdbc.cassandra.scalaz

import com.wda.sdbc.Cassandra._

import scalaz.concurrent.Task
import scalaz.stream.Process

trait ExecuteImplicits {

  implicit class ExecuteProcessMethods[T](execute: Execute) {
    def process(implicit pool: Pool): Process[Task, Unit] = {
      ExecuteProcess.forExecute(execute)
    }
  }

}
