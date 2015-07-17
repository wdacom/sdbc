package com.wda.sdbc.cassandra.scalaz

import com.datastax.driver.core.ResultSet
import com.wda.sdbc.cassandra
import com.wda.sdbc.Cassandra._
import scalaz._
import scalaz.concurrent.Task
import scalaz.stream._
import scala.collection.convert.wrapAsScala._
import com.google.common.util.concurrent.{FutureCallback, Futures}

object SelectProcess {

  def forPool[T]()(implicit pool: Pool): Channel[Task, Select[T], Process[Task, T]] = {

    channel.lift[Task, Select[T], Process[Task, T]] { select =>

      val acquire: Task[ResultSet] = {
        val statement = cassandra.prepare(
          statement = select.statement,
          parameterValues = select.parameterValues,
          queryOptions = select.queryOptions
        )

        /*
        See com.rocketfuel.kafka.streaming.Producer.channel for an explanation of why
        this is so convoluted.
         */
        def sendWithCallback(callback: Throwable \/ ResultSet => Unit): Unit = {

          val rsFuture = pool.executeAsync(statement)

          val googleCallback = new FutureCallback[ResultSet] {
            override def onFailure(t: Throwable): Unit = {
              callback(-\/(t))
            }

            override def onSuccess(result: ResultSet): Unit = {
              callback(\/-(result))
            }
          }

          Futures.addCallback(rsFuture, googleCallback)
        }

        Task.async(sendWithCallback)

      }

      Task(Process.await(acquire)(rs => IteratorToProcess(rs.iterator()).map(select.converter)))
    }
  }

  private def IteratorToProcess[T](iterator: Iterator[T]): Process[Task, T] = {
    Process.repeatEval(Task(if (iterator.hasNext) iterator.next() else throw Cause.Terminated(Cause.End)))
  }

}
