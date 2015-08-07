package com.wda.sdbc.cassandra.scalaz

import com.datastax.driver.core.{BoundStatement, ResultSet, Row => CRow}
import com.google.common.util.concurrent.{FutureCallback, Futures}
import com.wda.sdbc.Cassandra._

import scala.collection.convert.wrapAsScala._
import scalaz.concurrent.Task
import scalaz.{-\/, \/-}

trait ProcessMethods {

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

}
