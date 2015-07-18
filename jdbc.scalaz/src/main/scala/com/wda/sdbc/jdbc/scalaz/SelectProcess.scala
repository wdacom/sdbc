package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc
import scalaz.concurrent.Task
import scalaz.stream._

object SelectProcess {

  def forConnection[T]()(implicit connection: Connection): Channel[Task, jdbc.Select[T], Process[Task, T]] = {

    channel.lift[Task, jdbc.Select[T], Process[Task, T]] { select =>

      val acquire: Task[jdbc.Row] = Task {
        val statement = jdbc.prepare(
          queryText = select.queryText,
          parameterValues = select.parameterValues,
          parameterPositions = select.parameterPositions
        )
        new jdbc.Row(statement.executeQuery())
      }

      def release(row: jdbc.Row): Task[Unit] = Task(row.close())

      def step(row: jdbc.Row): Task[T] = Task {
        if (row.underlying.next()) {
          select.converter(row)
        } else {
          throw Cause.Terminated(Cause.End)
        }
      }

      Task(io.resource[Task, jdbc.Row, T](acquire)(release)(step))
    }
  }

  private case class TransactionResource(
    connection: Connection,
    row: jdbc.Row
  ) {
    def close(): Unit = {
      row.close()
      connection.close()
    }
  }

  def forPool[T](
    pool: jdbc.Pool
  ): Channel[Task, jdbc.Select[T], Process[Task, T]] = {

    channel.lift[Task, jdbc.Select[T], Process[Task, T]] { select =>
      val acquire: Task[TransactionResource] = Task {
        implicit val connection = pool.getConnection()
        val statement = jdbc.prepare(
          queryText = select.queryText,
          parameterValues = select.parameterValues,
          parameterPositions = select.parameterPositions
        )
        TransactionResource(connection, new jdbc.Row(statement.executeQuery()))
      }

      def release(resource: TransactionResource): Task[Unit] = {
        Task(resource.close())
      }

      def step(resource: TransactionResource): Task[T] = {
        val TransactionResource(_, row) = resource
          Task {
            if (row.underlying.next()) {
              select.converter(row)
            } else {
              throw Cause.Terminated(Cause.End)
            }
          }
      }

      Task(io.resource[Task, TransactionResource, T](acquire)(release)(step))
    }
  }

}
