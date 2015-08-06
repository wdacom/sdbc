package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc
import com.wda.sdbc.jdbc.ResultSetImplicits
import scalaz.concurrent.Task
import scalaz.stream._
import com.rocketfuel.scalaz.stream._

object SelectProcess extends ResultSetImplicits {

  private case class Resource[T](
    select: jdbc.Select[T]
  )(implicit connection: Connection) {

    val statement =
      jdbc.prepare(
        queryText = select.queryText,
        parameterValues = select.parameterValues,
        parameterPositions = select.parameterPositions
      )

    val resultSet = statement.executeQuery()

    def iterator(): Task[Iterator[T]] = {
      Task.delay(resultSet.iterator().map(select.converter))
    }

    def close(closeConnection: Boolean = true): Task[Unit] = Task {
      resultSet.close()

      statement.close()

      if (closeConnection) {
        connection.close()
      }
    }
  }

  def forSelect[T](select: jdbc.Select[T])(implicit connection: Connection) = {
    val acquire: Task[Resource[T]] = Task.delay(Resource[T](select))

    io.iterator[Resource[T], T](acquire)(_.iterator())(_.close(closeConnection = false))
  }

  /**
   * Run a stream of select statements. If the connection's autocommit is on, each select statement is
   * run in its own transaction, otherwise they are run in the same transaction.
   * @param connection
   * @tparam T
   * @return
   */
  def forConnection[T](implicit connection: Connection): Channel[Task, jdbc.Select[T], Process[Task, T]] = {
    channel.lift[Task, jdbc.Select[T], Process[Task, T]] { select =>
      val acquire: Task[Resource[T]] = Task.delay(Resource[T](select))

      Task.delay(io.iterator[Resource[T], T](acquire)(_.iterator())(_.close(closeConnection = false)))
    }
  }

  /**
   * Run a series of selects, each in its own connection and transaction.
   * Each statement is committed if the pool has autoCommit turned on.
   * @param pool
   * @tparam T
   * @return
   */
  def forPool[T](
    implicit pool: jdbc.Pool
  ): Channel[Task, jdbc.Select[T], Process[Task, T]] = {

    channel.lift[Task, jdbc.Select[T], Process[Task, T]] { select =>
      val acquire: Task[Resource[T]] = for {
        connection <- Task.delay(pool.getConnection())
        resource <- Task.delay(Resource[T](select)(connection))
      } yield resource

      Task(io.iterator[Resource[T], T](acquire)(_.iterator())(_.close()))
    }
  }

}
