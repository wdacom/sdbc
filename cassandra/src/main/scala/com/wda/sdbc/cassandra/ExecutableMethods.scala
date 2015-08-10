package com.wda.sdbc.cassandra

import com.wda.sdbc.base

import scala.concurrent.{ExecutionContext, Future}

trait ExecutableMethods extends base.ExecutableMethods[Session, Execute] {

  def executeAsync[Key](key: Key)(implicit ev: Executable[Key], connection: Session, ec: ExecutionContext): Future[Unit] = {
    ev.execute(key).executeAsync()
  }

}
