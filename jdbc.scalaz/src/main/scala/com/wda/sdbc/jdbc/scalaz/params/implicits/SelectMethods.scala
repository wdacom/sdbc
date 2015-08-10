package com.wda.sdbc.jdbc.scalaz.params.implicits

import com.wda.sdbc.jdbc._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait SelectMethods {
  implicit class SelectMethods[T](select: Select[T]) {
    def process()(implicit connection: Connection): Process[Task, T] = {
      Process.iterator(Task.delay(select.iterator()))
    }

    def processes()(implicit pool: Pool): Channel[Task, ParameterList, Process[Task, T]] = {
      channel.lift { params =>
        for {
          connection <- Task.delay(pool.getConnection())
        } yield {
          SelectMethods(select.on(params: _*)).process()(connection).onComplete(Process.eval_(Task.delay(connection.close())))
        }
      }
    }
  }
}
