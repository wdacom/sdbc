package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc
import scalaz.concurrent.Task
import scalaz.stream._

object UpdateProcess {

  def forConnection()(implicit connection: Connection): Channel[Task, jdbc.Update, Long] = {
    channel.lift[Task, jdbc.Update, Long] { update =>
      Task(update.update())
    }
  }

  def forPool(pool: jdbc.Pool): Channel[Task, jdbc.Update, Long] = {
    channel.lift[Task, jdbc.Update, Long] { update =>
      Task {
        pool.withConnection { implicit connection =>
          update.update()
        }
      }
    }
  }

}
