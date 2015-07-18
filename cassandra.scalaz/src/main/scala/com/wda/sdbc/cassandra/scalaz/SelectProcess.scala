package com.wda.sdbc.cassandra.scalaz

import com.datastax.driver.core.{BoundStatement, ResultSet, Row => CRow}
import com.wda.sdbc.cassandra
import com.wda.sdbc.Cassandra._
import scalaz._
import scalaz.concurrent.Task
import scalaz.stream._
import scala.collection.convert.wrapAsScala._
import com.google.common.util.concurrent.{FutureCallback, Futures}

object SelectProcess {

  private def runBoundStatement(
    prepared: BoundStatement
  )(implicit pool: Pool
  ): Task[Iterator[CRow]] = {
    Task.async { callback =>
      val rsFuture = pool.executeAsync(prepared)

      val googleCallback = new FutureCallback[ResultSet] {
        override def onFailure(t: Throwable): Unit = {
          callback(-\/(t))
        }

        override def onSuccess(result: ResultSet): Unit = {
          callback(\/-(result.iterator()))
        }
      }

      Futures.addCallback(rsFuture, googleCallback)
    }
  }

  def forPool[T](implicit pool: Pool): Channel[Task, Select[T], Process[Task, T]] = {
    channel.lift[Task, Select[T], Process[Task, T]] { select =>
      for {
        statement <- Task(cassandra.prepare(select))
        iteratorCreator = runBoundStatement(statement)
        process <- Task(IteratorToProcess(iteratorCreator))
      } yield {
        process.map(select.converter)
      }
    }
  }

  def IteratorToProcess[F[_], O](iteratorCreator: F[Iterator[O]]): Process[F, O] = {
    //This design was based on unfold.
    def go(iterator: Iterator[O]): Process0[O] = {
      if (iterator.hasNext) Process.emit(iterator.next()) ++ go(iterator)
      else Process.halt
    }

    Process.await(iteratorCreator)(go)
  }

}
