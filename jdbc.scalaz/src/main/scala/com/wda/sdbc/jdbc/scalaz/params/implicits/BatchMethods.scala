package com.wda.sdbc.jdbc.scalaz.params.implicits

import com.wda.sdbc.jdbc.{Batch, Connection, ParameterList, Pool}
import me.jeffshaw.scalaz.stream.IteratorConstructors._

import scalaz.concurrent.Task
import scalaz.stream._

trait BatchMethods {
  implicit class BatchMethods(batch: Batch) {
    def process()(implicit connection: Connection): Process[Task, Long] = {
      Process.iterator(Task.delay(batch.iterator()(connection)))
    }

    def processes()(implicit pool: Pool): Channel[Task, Seq[ParameterList], Seq[Long]] = {
      channel.lift { batches =>
        for {
          connection <- Task.delay(pool.getConnection())
          result <- Task.delay(batches.foldLeft(batch){case (b, params) => b.addBatch(params: _*)}.seq()(connection)).onFinish(_ => Task.delay(connection.close()))
        } yield result
      }
    }
  }
}
