package com.wda.sdbc.jdbc.scalaz.params.implicits

import com.wda.sdbc.jdbc._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait ExecuteMethods {
  implicit class ExecuteMethods(execute: Execute) {
    def process()(implicit connection: Connection): Process[Task, Unit] = {
      Process.eval(Task.delay(execute.execute()(connection)))
    }

    def processes()(implicit pool: Pool): Sink[Task, ParameterList] = {
      sink.lift { params =>
        for {
          connection <- Task.delay(pool.getConnection())
          _ <- Task.delay(execute.on(params: _*).execute()(connection)).onFinish(_ => Task.delay(connection.close()))
        } yield ()
      }
    }
  }
}
