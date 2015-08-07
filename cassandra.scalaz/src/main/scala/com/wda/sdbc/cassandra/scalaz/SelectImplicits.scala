package com.wda.sdbc.cassandra.scalaz

import com.wda.sdbc.Cassandra._
import com.wda.sdbc.cassandra
import scalaz.stream._
import scalaz.concurrent.Task
import me.jeffshaw.scalaz.stream.IteratorConstructors._

trait SelectImplicits {

  implicit class SelectProcessMethods[T](select: Select[T]) {
    def process(implicit pool: Pool): Process[Task, T] = {
      Process.iterator[T](runBoundStatement(cassandra.prepare(select)).map(_.map(select.converter)))
    }
  }

}
