package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection
import com.wda.sdbc.jdbc.Select
import scalaz.concurrent.Task
import scalaz.stream._

trait SelectImplicits {

  implicit class SelectProcessMethods[T](select: Select[T]) {
    def process(implicit connection: Connection): Process[Task, T] = {
      SelectProcess.forSelect[T](select)
    }
  }

}
