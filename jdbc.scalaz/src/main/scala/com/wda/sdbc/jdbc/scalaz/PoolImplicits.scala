package com.wda.sdbc.jdbc.scalaz

import com.wda.sdbc.jdbc.{Pool, Select, Update}

import scalaz.concurrent.Task
import scalaz.stream._

trait PoolImplicits {

  implicit class PoolChannels(pool: Pool) {
    lazy val updates: Channel[Task, Update, Long] = {
      UpdateProcess.forPool(pool)
    }

    def selects[T]: Channel[Task, Select[T], Process[Task, T]] = {
      SelectProcess.forPool[T](pool)
    }
  }

}
