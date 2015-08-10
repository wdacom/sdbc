package com.wda.sdbc.jdbc.scalaz

import com.wda.sdbc.jdbc._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

object JdbcProcess {

  object jdbc {
    def execute(execute: Execute)(implicit connection: Connection): Process[Task, Unit] = {
      Process.eval(Task.delay(execute.execute()))
    }

    def batch(batch: Batch)(implicit connection: Connection): Process[Task, Seq[Long]] = {
      Process.eval(Task.delay(batch.seq()))
    }

    def select[T](select: Select[T])(implicit connection: Connection): Process[Task, T] = {
      Process.iterator(Task.delay(select.iterator()))
    }

    def update(update: Update)(implicit connection: Connection): Process[Task, Long] = {
      Process.eval(Task.delay(update.update()))
    }

    object params {
      def batch(batch: Batch)(implicit pool: Pool): Channel[Task, Traversable[ParameterList], Seq[Long]] = {
        channel.lift { batches =>
          for {
            connection <- Task.delay(pool.getConnection())
            result <- Task.delay(batches.foldLeft(batch){case (b, params) => b.addBatch(params: _*)}.seq()(connection)).onFinish(_ => Task.delay(connection.close()))
          } yield result
        }
      }

      def execute(execute: Execute)(implicit pool: Pool): Sink[Task, ParameterList] = {
        sink.lift { params =>
          for {
            connection <- Task.delay(pool.getConnection())
            _ <- Task.delay(execute.on(params: _*).execute()(connection)).onFinish(_ => Task.delay(connection.close()))
          } yield ()
        }
      }

      def select[T](select: Select[T])(implicit pool: Pool): Channel[Task, ParameterList, Process[Task, T]] = {
        channel.lift { params =>
          for {
            connection <- Task.delay(pool.getConnection())
          } yield {
            Process.iterator(Task.delay(select.on(params: _*).iterator()(connection))).onComplete(Process.eval_(Task.delay(connection.close())))
          }
        }
      }

      def update(update: Update)(implicit pool: Pool): Channel[Task, ParameterList, Long] = {
        channel.lift { params =>
          for {
            connection <- Task.delay(pool.getConnection())
            updates <- Task.delay(update.update()(connection)).onFinish(_ => Task.delay(connection.close()))
          } yield updates
        }
      }
    }

    object keys {
      private def getConnection(pool: Pool) = Task.delay(pool.getConnection())
      private def closeConnection(connection: Connection): Task[Unit] = Task.delay(connection.close())

      private def withConnection[Key, T](pool: Pool)(task: Key => Connection => Task[T]): Channel[Task, Key, T] = {
        channel.lift { key =>
          for {
            connection <- getConnection(pool)
            result <- task(key)(connection).onFinish(_ => closeConnection(connection))
          } yield result
        }
      }


      def batch[Key](pool: Pool)(implicit batchable: Batchable[Key]): Channel[Task, Key, Seq[Long]] = {
        withConnection[Key, Seq[Long]](pool) { key => implicit connection =>
          Task.delay(batchable.batch(key).seq())
        }
      }

      def execute[Key](pool: Pool)(implicit executable: Executable[Key]): Sink[Task, Key] = {
        withConnection[Key, Unit](pool) { key => implicit connection =>
          Task.delay(Task.delay(executable.execute(key).execute()))
        }
      }

      def select[Key, Value](pool: Pool)(implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
        channel.lift { key =>
          for {
            connection <- getConnection(pool)
            select <- Task.delay(selectable.select(key))
            results <- Task.delay(Process.iterator(Task.delay(select.iterator()(connection))).onComplete(Process.eval_(closeConnection(connection))))
          } yield results
        }
      }

      def update[Key](pool: Pool)(implicit updatable: Updatable[Key]): Channel[Task, Key, Long] = {
        withConnection[Key, Long](pool) { key => implicit connection =>
          Task.delay(updatable.update(key).update())
        }
      }
    }
  }
}
