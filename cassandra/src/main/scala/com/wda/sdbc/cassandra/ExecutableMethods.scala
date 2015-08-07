package com.wda.sdbc.cassandra

import com.datastax.driver.core.Session
import com.wda.sdbc.{base, cassandra}

import scala.concurrent.{ExecutionContext, Future}

trait ExecutableMethods extends base.ExecutableMethods[Session] {

  override type Execute = cassandra.Execute

  type Executable[Key] = base.Executable[Session, Key, Execute]

  def executeAsync[Key](key: Key)(implicit ev: Executable[Key], connection: Session, ec: ExecutionContext): Future[Unit] = {
    ev.execute(key).executeAsync()
  }

}
