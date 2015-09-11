package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.rocketfuel.sdbc.cassandra.datastax
import com.datastax.driver.core
import com.rocketfuel.sdbc.base

import scala.concurrent.{ExecutionContext, Future}

private[sdbc] trait ExecutableMethods extends base.ExecutableMethods[core.Session, datastax.Execute] {

  def executeAsync[Key](
    key: Key
  )(implicit ev: datastax.Executable[Key],
    connection: datastax.Session,
    ec: ExecutionContext
  ): Future[Unit] = {
    ev.execute(key).executeAsync()
  }

}
