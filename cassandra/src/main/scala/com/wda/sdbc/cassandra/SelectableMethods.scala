package com.wda.sdbc.cassandra

import com.datastax.driver.core.Session
import com.wda.sdbc.{cassandra, base}

import scala.concurrent.{ExecutionContext, Future}

trait SelectableMethods extends base.SelectableMethods[Session] {

  override type Select[T] = cassandra.Select[T]

  type Selectable[Key, Value] = base.Selectable[Session, Key, Value, Select[Value]]

  def iteratorAsync[Key, Value](key: Key)(implicit ev: Selectable[Key, Value], connection: Session, ec: ExecutionContext): Future[Iterator[Value]] = {
    ev.select(key).iteratorAsync()
  }

}
