package com.wda.sdbc.cassandra.datastax

import com.datastax.driver.core
import com.wda.sdbc.base

import scala.concurrent.{ExecutionContext, Future}

trait ExecutableMethods extends base.ExecutableMethods[core.Session, Execute] {

  def executeAsync[Key](
    key: Key
  )(implicit ev: Executable[Key],
    connection: Session,
    ec: ExecutionContext
  ): Future[Unit] = {
    ev.execute(key).executeAsync()
  }

}
