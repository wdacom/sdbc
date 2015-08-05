package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection
import com.wda.sdbc.jdbc.Update
import scalaz.concurrent.Task
import scalaz.stream.Process

trait UpdateImplicits {

  implicit class UpdateProcessMethods[T](update: Update) {
    def process(implicit connection: Connection): Process[Task, Long] = {
      UpdateProcess.forUpdate(update)
    }
  }
  
}
