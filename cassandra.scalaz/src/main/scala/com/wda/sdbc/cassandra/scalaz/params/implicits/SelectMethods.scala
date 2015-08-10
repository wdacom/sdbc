package com.wda.sdbc.cassandra.scalaz.params.implicits

import com.wda.sdbc.cassandra._

import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._

trait SelectMethods {
  implicit class SelectMethods[T](select: Select[T]) {
    def process()(implicit session: Session): Task[Process[Task, T]] = {
      scalaz.runSelect(select)
    }

    def processes()(implicit session: Session): Channel[Task, ParameterList, Process[Task, T]] = {
      channel.lift(params => scalaz.runSelect[T](select.on(params: _*)))
    }
  }
}
