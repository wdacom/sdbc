package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc
import scalaz.concurrent.Task
import scalaz.stream._

object UpdateProcess {

  def forUpdate(update: jdbc.Update)(implicit connection: Connection): Process[Task, Long] = {
    Process.eval[Task, Long](Task(update.update()))
  }

  /**
   * Run a stream of update statements. If the connection's autocommit is on, each select statement is
   * run in its own transaction, otherwise they are run in the same transaction.
   * @param connection
   * @return
   */
  def forConnection(implicit connection: Connection): Channel[Task, jdbc.Update, Long] = {
    channel.lift[Task, jdbc.Update, Long] { update =>
      Task(update.update())
    }
  }

  /**
   * Run a series of updates, each in its own connection and transaction.
   * @param pool
   * @return
   */
  def forPool(implicit pool: jdbc.Pool): Channel[Task, jdbc.Update, Long] = {
    channel.lift[Task, jdbc.Update, Long] { update =>
      Task {
        pool.withConnection { implicit connection =>
          update.update()
        }
      }
    }
  }

}
