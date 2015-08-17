package com.wda.sdbc.cassandra

import com.wda.sdbc.base

import scala.concurrent.{ExecutionContext, Future}

trait SelectableMethods extends base.SelectableMethods[Session, Select] {

  def iteratorAsync[Key, Value](key: Key)(implicit ev: Selectable[Key, Value], connection: Session, ec: ExecutionContext): Future[Iterator[Value]] = {
    ev.select(key).iteratorAsync()
  }

}
