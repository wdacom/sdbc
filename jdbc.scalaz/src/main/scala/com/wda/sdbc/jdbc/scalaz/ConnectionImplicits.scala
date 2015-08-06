package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc.{Select, Update}
import scalaz.concurrent.Task
import scalaz.stream._

trait ConnectionImplicits {

  implicit class ConnectionChannels(connection: Connection) {
    val updateProcess: Channel[Task, Update, Long] = {
      UpdateProcess.forConnection(connection)
    }

    def selectProcess[T]: Channel[Task, Select[T], Process[Task, T]] = {
      SelectProcess.forConnection[T](connection)
    }
  }

}
