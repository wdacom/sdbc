package com.wda.sdbc.jdbc.scalaz.key.implicits

import com.wda.sdbc.jdbc._
import me.jeffshaw.scalaz.stream.IteratorConstructors._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait PoolMethods {
  self: ConnectionMethods =>

  implicit class PoolMethods(pool: Pool) {
    val getConnection = Task.delay(pool.getConnection())
    def closeConnection(connection: Connection): Task[Unit] = Task.delay(connection.close())

    def withConnection[Key, T](task: Key => Connection => Task[T]): Channel[Task, Key, T] = {
      channel.lift { key =>
        for {
          connection <- getConnection
          result <- task(key)(connection).onFinish(_ => closeConnection(connection))
        } yield result
      }
    }


    def batches[Key]()(implicit batchable: Batchable[Key]): Channel[Task, Key, Process[Task, Long]] = {
      withConnection[Key, Process[Task, Long]] { key => implicit connection =>
        Task.delay(Process.iterator(Task.delay(batchable.batch(key).iterator())))
      }
    }

    def executes[Key]()(implicit executable: Executable[Key]): Sink[Task, Key] = {
      withConnection[Key, Unit] { key => implicit connection =>
        Task.delay(Task.delay(executable.execute(key).execute()))
      }
    }

    def selects[Key, Value]()(implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
      channel.lift { key =>
        for {
          connection <- getConnection
          select <- Task.delay(selectable.select(key))
          results <- Task.delay(Process.iterator(Task.delay(select.iterator()(connection))).onComplete(Process.eval_(closeConnection(connection))))
        } yield results
      }
    }

    def updates[Key]()(implicit updatable: Updatable[Key]): Channel[Task, Key, Long] = {
      Process.await(Task.delay(pool.getConnection())) { connection =>
        connection.updates.onComplete(Process.eval_(Task.delay(connection.close())))
      }
    }

  }

}
