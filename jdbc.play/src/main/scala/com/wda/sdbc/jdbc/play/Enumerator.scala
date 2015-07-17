package com.wda.sdbc.jdbc.play

import com.wda.sdbc.jdbc.{Pool, Select}
import play.api.libs.iteratee.{Enumerator => PEnumerator}
import scala.concurrent.{ExecutionContext, Future}

object Enumerator {

  def apply[T](
    select: Select[T],
    pool: Pool
  )(implicit ec: ExecutionContext
  ): PEnumerator[T] = {
    implicit val connection = pool.getConnection()
    val iterator = select.iterator()
    PEnumerator.generateM[T] {
      Future {
        if (iterator.hasNext) Some(iterator.next())
        else {
          connection.close()
          None
        }
      }
    }
  }

}
