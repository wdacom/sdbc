package com.wda.sdbc.jdbc.scalaz

import com.wda.sdbc.jdbc._
import _root_.scalaz.concurrent.Task
import _root_.scalaz.stream._
import me.jeffshaw.scalaz.stream.IteratorConstructors._

object ProcessMethods {

  def execute(execute: Execute)(implicit connection: Connection): Process[Task, Unit] = {
    Process.eval(Task.delay(execute.execute()))
  }

  def batch(batch: Batch)(implicit connection: Connection): Process[Task, Seq[Long]] = {
    Process.eval(Task.delay(batch.seq()))
  }

  def select[T](select: Select[T])(implicit connection: Connection): Process[Task, T] = {
    Process.iterator(Task.delay(select.iterator()))
  }

  def update(update: Update)(implicit connection: Connection): Process[Task, Long] = {
    Process.eval(Task.delay(update.update()))
  }

}
