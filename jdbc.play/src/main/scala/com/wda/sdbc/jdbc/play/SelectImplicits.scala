package com.wda.sdbc.jdbc.play

import com.wda.sdbc.jdbc.{Pool, Select}
import play.api.libs.iteratee.{Enumerator => PEnumerator}

import scala.concurrent.ExecutionContext

trait SelectImplicits {

  implicit class EnumeratorSupport[T](select: Select[T]) {
    def enumerator(
      pool: Pool
    )(implicit ec: ExecutionContext
    ): PEnumerator[T] = {
      Enumerator[T](select, pool)
    }
  }

}
