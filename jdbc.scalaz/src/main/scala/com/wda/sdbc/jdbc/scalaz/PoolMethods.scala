package com.wda.sdbc.jdbc.scalaz

import com.wda.sdbc.jdbc.DBMS

import scalaz.concurrent.Task
import scalaz.stream._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

trait PoolMethods {

  implicit class PoolMethods(pool: DBMS#Pool) {
    def executes[Key](implicit executable: DBMS#Executable[Key]): Sink[Task, Key] = {
      sink.lift[Task, Key] { key =>
        for {
        connection <- Task.delay(pool.getConnection())
        _ <- Task.delay(executable.execute(key).execute()(connection))
      } yield {
          connection.close()
        }
      }
    }

    def selects[Key, Value](implicit selectable: DBMS#Selectable[Key, Value]): Channel[Task, Key, Process[Task, Value]] = {
      channel.lift[Task, Key, Process[Task, Value]] { key =>
        Task.delay(
          Process.await(Task.delay(pool.getConnection())) {implicit connection =>
            Process.iterator(Task.delay(selectable.select(key).iterator())).onComplete(Process.eval_(Task.delay(connection.close())))
          }
        )
      }
    }

    def updates[Key](implicit updatable: DBMS#Updatable[Key]): Channel[Task, Key, Long] = {
      channel.lift[Task, Key, Long] { key =>
        for {
          connection <- Task.delay(pool.getConnection())
          result <- Task.delay(updatable.update(key).update()(connection))
        } yield {
          connection.close()
          result
        }
      }
    }
  }
}
