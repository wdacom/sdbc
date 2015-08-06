package com.wda.sdbc.cassandra.scalaz

import com.wda.sdbc.Cassandra._
import scalaz.concurrent.Task
import scalaz.stream._

trait PoolImplicits {

  implicit class PoolChannels(pool: Pool) {
    def process[T]: Channel[Task, Select[T], Process[Task, T]] = {
      SelectProcess.forPool[T](pool)
    }
  }

}
