package com.wda.sdbc.cassandra.scalaz

import com.wda.sdbc.Cassandra._
import scalaz.stream._
import scalaz.concurrent.Task

trait SelectImplicits {

  implicit class SelectProcessMethods[T](select: Select[T]) {
    def process(implicit pool: Pool): Process[Task, T] = {
      SelectProcess.forSelect[T](select)
    }
  }

}
