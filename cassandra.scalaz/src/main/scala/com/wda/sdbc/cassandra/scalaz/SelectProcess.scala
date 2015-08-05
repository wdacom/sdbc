package com.wda.sdbc.cassandra.scalaz

import com.datastax.driver.core.{Row => CRow, Cluster, BoundStatement, ResultSet}
import com.wda.sdbc.cassandra
import com.wda.sdbc.Cassandra._
import scalaz._
import scalaz.concurrent.Task
import scalaz.stream._
import com.rocketfuel.scalaz.stream._
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

  def forSelect[T](select: Select[T])(implicit pool: Pool): Process[Task, T] = {
    Process.iterator[T](runBoundStatement(cassandra.prepare(select)).map(_.map(select.converter)))
  }

  def forPool[T](implicit pool: Pool): Channel[Task, Select[T], Process[Task, T]] = {
    channel.lift[Task, Select[T], Process[Task, T]] { select =>
      for {
        statement <- Task.delay(cassandra.prepare(select))
      } yield {
        val iteratorCreator = runBoundStatement(statement)
        Process.iterator(iteratorCreator).map(select.converter)
      }
    }
  }

  def forCluster[T](implicit cluster: Cluster): Channel[Task, Select[T], Process[Task, T]] = {
    channel.lift[Task, Select[T], Process[Task, T]] { select =>
      Task(io.iterator[Pool, T](Task.delay(cluster.connect()))(implicit p => Task(select.iterator()))(p => Task.delay(p.close())))
    }
  }

}
