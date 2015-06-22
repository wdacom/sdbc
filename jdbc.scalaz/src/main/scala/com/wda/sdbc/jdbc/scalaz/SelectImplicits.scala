package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc.{Pool, Select}
import scalaz.stream._
import scalaz.Applicative

trait SelectImplicits {

  implicit class ProcessSupport[T](select: Select[T]) {
    def process[F[_]](implicit connection: Connection, ev: Applicative[F]): Process[F, T] = {
      Process[F, T](select)
    }

    def processTransaction[F[_]](
      pool: Pool
    )(implicit ev: Applicative[F]
    ): Process[F, T] = {
      Process.transaction[F, T](select, pool)
    }
  }

}
