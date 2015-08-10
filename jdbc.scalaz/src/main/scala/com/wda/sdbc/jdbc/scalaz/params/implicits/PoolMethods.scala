package com.wda.sdbc.jdbc.scalaz.params.implicits

import com.wda.sdbc.jdbc._
import me.jeffshaw.scalaz.stream.IteratorConstructors._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait PoolMethods {

  implicit class PoolMethods(pool: Pool) {
    val getConnection = Task.delay(pool.getConnection())
    def closeConnection(connection: Connection): Task[Unit] = Task.delay(connection.close())

    def withConnection[T](task: ParameterList => Connection => Task[T]): Channel[Task, ParameterList, T] = {
      channel.lift { params =>
        for {
          connection <- getConnection
          result <- task(params)(connection).onFinish(_ => closeConnection(connection))
        } yield result
      }
    }

    def batches(batch: Batch): Channel[Task, ParameterList, Seq[Long]] = {
      withConnection[Seq[Long]] { params => implicit connection =>
        Task.delay(batch.addBatch(params: _*).seq())
      }
    }

    def executes(execute: Execute): Sink[Task, ParameterList] = {
      withConnection[Unit] { params => implicit connection =>
        Task.delay(execute.on(params: _*).execute())
      }
    }

    def selects[Value](select: Select[Value]): Channel[Task, ParameterList, Process[Task, Value]] = {
      channel.lift { params =>
        Task.delay {
          Process.await(getConnection) {implicit connection =>
            Process.iterator(Task.delay(select.on(params: _*).iterator())).onComplete(Process.eval_(closeConnection(connection)))
          }
        }
      }
    }

    def updates(update: Update): Channel[Task, ParameterList, Long] = {
      withConnection[Long] { params => implicit connection =>
        Task.delay(update.on(params: _*).update())
      }
    }
  }

}
