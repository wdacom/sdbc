package com.wda.sdbc.cassandra.scalaz.params.implicits

import com.wda.sdbc.cassandra._
import com.wda.sdbc.cassandra.scalaz._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait PoolMethods {
  implicit class PoolMethods(pool: Session) {
    def executes(execute: Execute): Sink[Task, ParameterList] = {
      sink.lift[Task, Seq[(String, Option[ParameterValue[_]])]] { params =>
        runExecute(execute.on(params: _*))(pool)
      }
    }

    def selects[Value](select: Select[Value]): Channel[Task, ParameterList, Process[Task, Value]] = {
      channel.lift[Task, Seq[(String, Option[ParameterValue[_]])], Process[Task, Value]] { params =>
        runSelect[Value](select.on(params: _*))(pool)
      }
    }
  }
}
