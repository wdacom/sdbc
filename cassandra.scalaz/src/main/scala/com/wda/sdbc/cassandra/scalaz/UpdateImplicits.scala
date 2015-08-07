package com.wda.sdbc.cassandra.scalaz

import com.wda.sdbc.Cassandra._

import scalaz.concurrent.Task
import scalaz.stream.Process

trait UpdateImplicits {

  implicit class UpdateProcessMethods[T](update: Update) {
    def process(implicit pool: Pool): Process[Task, Unit] = {
      UpdateProcess.forUpdate(update)
    }
  }

}
