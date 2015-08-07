package com.wda.sdbc.cassandra

import java.util.concurrent.atomic.AtomicReference
import java.util.function.UnaryOperator

import com.datastax.driver.core.{Row => CRow, Cluster, BoundStatement, ResultSet}
import com.google.common.util.concurrent.{FutureCallback, Futures}
import com.wda.sdbc.Cassandra
import com.wda.sdbc.Cassandra._

import scala.collection.convert.wrapAsScala._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.{-\/, \/-}
import _root_.scalaz.stream._

package object scalaz {

  protected def runBoundStatement(
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

  protected def ignoreBoundStatement(
    prepared: BoundStatement
  )(implicit pool: Pool
  ): Task[Unit] = {
    Task.async { callback =>
      val rsFuture = pool.executeAsync(prepared)

      val googleCallback = new FutureCallback[ResultSet] {
        override def onFailure(t: Throwable): Unit = {
          callback(-\/(t))
        }

        override def onSuccess(result: ResultSet): Unit = {
          callback(\/-(()))
        }
      }

      Futures.addCallback(rsFuture, googleCallback)
    }
  }


  protected def closePool(pool: Pool): Task[Unit] = {
    Task.async[Unit] { callback =>
      val rsFuture = pool.closeAsync()

      val googleCallback = new FutureCallback[Void] {
        override def onFailure(t: Throwable): Unit = {
          callback(-\/(t))
        }

        override def onSuccess(result: Void): Unit = {
          callback(\/-(()))
        }
      }

      Futures.addCallback(rsFuture, googleCallback)
    }
  }

  def forClusterWithKeyspaceAux[T, O](runner: T => Pool => Task[O])(implicit cluster: Cluster): Channel[Task, (String, T), O] = {

    val poolsRef = new AtomicReference(Map.empty[String, Pool])

    /**
     * Get a Pool for the keyspace, creating it if it does not exist.
     * @param keySpace
     * @return
     */
    def getPool(keySpace: String): Task[Pool] = Task.delay {
      val pools =
        poolsRef.updateAndGet(
          new UnaryOperator[Map[String, Pool]] {
            override def apply(t: Map[String, Cassandra.Pool]): Map[String, Cassandra.Pool] = {
              if (t.contains(keySpace)) t
              else {
                val pool = cluster.connect(keySpace)
                t + (keySpace -> pool)
              }
            }
          }
        )

      pools(keySpace)
    }

    /**
     * Empty the pools collection, and close all the pools.
     */
    val closePools: Task[Unit] = {
      val getToClose = Task.delay[Map[String, Pool]] {
        poolsRef.getAndUpdate(
          new UnaryOperator[Map[String, Pool]] {
            override def apply(t: Map[String, Cassandra.Pool]): Map[String, Cassandra.Pool] = {
              Map.empty
            }
          }
        )
      }

      for {
        toClose <- getToClose
        _ <- Task.gatherUnordered(toClose.map(kvp => closePool(kvp._2)).toSeq)
      } yield ()
    }

    channel.lift[Task, (String, T), O]{ case (keyspace, thing) =>
      for {
        pool <- getPool(keyspace)
        result <- runner(thing)(pool)
      } yield result
    }.onComplete(Process.eval_(closePools))
  }

}
