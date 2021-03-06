package com.rocketfuel.sdbc.scalaz

import com.rocketfuel.sdbc.base.jdbc._
import scalaz.concurrent.Task
import scalaz.stream._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

object JdbcProcess {

  object jdbc {
    private def getConnection(pool: Pool) = Task.delay(pool.getConnection())

    private def closeConnection(connection: Connection): Task[Unit] = Task.delay(connection.close())

    private def withConnection[Key, T](task: Key => Connection => Task[T])(implicit pool: Pool): Channel[Task, Key, T] = {
      channel.lift[Task, Key, T] { params =>
        for {
          connection <- getConnection(pool)
          result <- task(params)(connection).onFinish(_ => closeConnection(connection))
        } yield result
      }
    }

    /**
     * Create a Process with a single Unit value.
     *
     * The connection is not closed when the Process completes.
     * @param execute
     * @param connection
     * @return
     */
    def execute(
      execute: Execute
    )(implicit connection: Connection
    ): Process[Task, Unit] = {
      Process.eval(Task.delay(execute.execute()))
    }

    /**
     * Create a Process with a single Seq[Long] value, indicating the number of
     * updated rows per query in the batch.
     *
     * The connection is not closed when the Process completes.
     * @param batch
     * @param connection
     * @return
     */
    def batch(
      batch: Batch
    )(implicit connection: Connection
    ): Process[Task, Seq[Long]] = {
      Process.eval(Task.delay(batch.seq()))
    }

    /**
     * Create a Process of the query results.
     *
     * The connection is not closed when the Process completes.
     * @param select
     * @param connection
     * @tparam T
     * @return
     */
    def select[T](
      select: Select[T]
    )(implicit connection: Connection
    ): Process[Task, T] = {
      Process.iterator(Task.delay(select.iterator()))
    }

    /**
     * Create a Process with one element indicating the number of updated rows.
     *
     * The connection is not closed when the Process completes.
     * @param update
     * @param connection
     * @return
     */
    def update(
      update: Update
    )(implicit connection: Connection
    ): Process[Task, Long] = {
      Process.eval(Task.delay(update.update()))
    }

    object params {

      /**
       * From a stream of collections of parameter lists, create a Process of single Seq[Long] values,
       * indicating the number of updated rows per query in the batch.
       *
       * A connection is taken from the pool for each execution.
       * @param batch
       * @return
       */
      def batch(batch: Batch)(implicit pool: Pool): Channel[Task, Traversable[ParameterList], Seq[Long]] = {
        withConnection[Traversable[ParameterList], Seq[Long]] { batches => implicit connection =>
          Task.delay(batches.foldLeft(batch){case (b, params) => b.addBatch(params: _*)}.seq())
        }
      }

      /**
       * From a stream of parameter lists, independently add each list to the
       * query, execute it, and ignore the results.
       *
       * A connection is taken from the pool for each execution.
       * @param execute
       * @return
       */
      def execute(execute: Execute)(implicit pool: Pool): Sink[Task, ParameterList] = {
        withConnection[ParameterList, Unit] { params => implicit connection =>
          Task.delay(execute.on(params: _*).execute())
        }
      }

      /**
       * From a stream of parameter lists, independently add each list to the
       * query, execute it, and create a stream of the results.
       *
       * Use merge.mergeN to run the queries in parallel, or
       * .flatMap(identity) to concatenate them.
       *
       * A connection is taken from the pool for each execution.
       * @param select
       * @param pool
       * @tparam T
       * @return
       */
      def select[T](
        select: Select[T]
      )(implicit pool: Pool
      ): Channel[Task, ParameterList, Process[Task, T]] = {
        channel.lift[Task, ParameterList, Process[Task, T]] { params =>
          Task.delay {
            Process.await(getConnection(pool)) {implicit connection =>
              Process.iterator(Task.delay(select.on(params: _*).iterator())).onComplete(Process.eval_(closeConnection(connection)))
            }
          }
        }
      }

      /**
       * From a stream of parameter lists, independently add each list to the
       * query, execute it, and obtain a count of the number of rows that were
       * updated.
       *
       * A connection is taken from the pool for each execution.
       * @param update
       * @return
       */
      def update(update: Update)(implicit pool: Pool): Channel[Task, ParameterList, Long] = {
        withConnection[ParameterList, Long] { params => implicit connection =>
          Task.delay(update.on(params: _*).update())
        }
      }
    }

    object keys {

      /**
       * Use an instance of Batchable to create a stream of Seq[Long], which each
       * indicates the number of rows updated by each query in the batch.
       *
       * A connection is taken from the pool for each execution.
       * @param pool
       * @param batchable
       * @tparam Key
       * @return
       */
      def batch[Key](
        pool: Pool
      )(implicit batchable: Batchable[Key]
      ): Channel[Task, Key, Seq[Long]] = {
        withConnection[Key, Seq[Long]] { key => implicit connection =>
          Task.delay(batchable.batch(key).seq())
        }(pool)
      }

      /**
       * Use an instance of Executable to create a stream of (),
       * which each indicates that a query was executed.
       *
       * A connection is taken from the pool for each execution.
       * @param pool
       * @param executable
       * @tparam Key
       * @return
       */
      def execute[Key](
        pool: Pool
      )(implicit executable: Executable[Key]
      ): Sink[Task, Key] = {
        withConnection[Key, Unit] { key => implicit connection =>
          Task.delay(Task.delay(executable.execute(key).execute()))
        }(pool)
      }

      /**
       * Use an instance of Selectable to create a Process of a Process of
       * results.
       *
       * Use merge.mergeN on the result to run the queries in parallel, or .flatMap(identity)
       * to concatenate them.
       *
       * A connection is taken from the pool for each execution.
       * @param pool
       * @param selectable
       * @tparam Key
       * @tparam Value
       * @return
       */
      def select[Key, Value](
        pool: Pool
      )(implicit selectable: Selectable[Key, Value]
      ): Channel[Task, Key, Process[Task, Value]] = {
        channel.lift[Task, Key, Process[Task, Value]] { key =>
          Task.delay {
            Process.await(getConnection(pool)) {implicit connection =>
              Process.iterator(Task.delay(selectable.select(key).iterator())).onComplete(Process.eval_(closeConnection(connection)))
            }
          }
        }
      }

      /**
       * Use an instance of Updatable to create a Process indicating
       * how many rows were updated for each execution.
       *
       * A connection is taken from the pool for each execution.
       * @param pool
       * @param updatable
       * @tparam Key
       * @return
       */
      def update[Key](
        pool: Pool
      )(implicit updatable: Updatable[Key]
      ): Channel[Task, Key, Long] = {
        withConnection[Key, Long] { key => implicit connection =>
          Task.delay(updatable.update(key).update())
        }(pool)
      }
    }
  }
}
