package com.wda.sdbc.jdbc.scalaz

import com.wda.sdbc.jdbc._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

object JdbcProcess {

  object jdbc {
    private def getConnection(pool: Pool) = Task.delay(pool.getConnection())

    private def closeConnection(connection: Connection): Task[Unit] = Task.delay(connection.close())

    private def withConnection[Key, T](pool: Pool)(task: Key => Connection => Task[T]): Channel[Task, Key, T] = {
      channel.lift[Task, Key, T] { params =>
        for {
          connection <- getConnection(pool)
          result <- task(params)(connection).onFinish(_ => closeConnection(connection))
        } yield result
      }
    }

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

      def batch(pool: Pool)(batch: Batch): Channel[Task, Traversable[ParameterList], Seq[Long]] = {
        withConnection[Traversable[ParameterList], Seq[Long]](pool) { batches => implicit connection =>
          Task.delay(batches.foldLeft(batch){case (b, params) => b.addBatch(params: _*)}.seq())
        }
      }

      def execute(pool: Pool)(execute: Execute): Sink[Task, ParameterList] = {
        withConnection[ParameterList, Unit](pool) { params => implicit connection =>
          Task.delay(execute.on(params: _*).execute())
        }
      }

      def select[T](pool: Pool)(select: Select[T]): Channel[Task, ParameterList, Process[Task, T]] = {
        channel.lift[Task, ParameterList, Process[Task, T]] { params =>
          Task.delay {
            Process.await(getConnection(pool)) {implicit connection =>
              Process.iterator(Task.delay(select.on(params: _*).iterator())).onComplete(Process.eval_(closeConnection(connection)))
            }
          }
        }
      }

      def update(pool: Pool)(update: Update): Channel[Task, ParameterList, Long] = {
        withConnection[ParameterList, Long](pool) { params => implicit connection =>
          Task.delay(update.on(params: _*).update())
        }
      }
    }

    object keys {

      def batch[Key](pool: Pool)(implicit batchable: Batchable[Key]): Channel[Task, Key, Seq[Long]] = {
        withConnection(pool) { key => implicit connection =>
          Task.delay(batchable.batch(key).seq())
        }
      }

      def execute[Key](pool: Pool)(implicit executable: Executable[Key]): Sink[Task, Key] = {
        withConnection[Key, Unit](pool) { key => implicit connection =>
          Task.delay(Task.delay(executable.execute(key).execute()))
        }
      }

      def select[Key, Value](pool: Pool)(implicit selectable: Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
        channel.lift[Task, Key, Process[Task, Value]] { key =>
          Task.delay {
            Process.await(getConnection(pool)) {implicit connection =>
              Process.iterator(Task.delay(selectable.select(key).iterator())).onComplete(Process.eval_(closeConnection(connection)))
            }
          }
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
