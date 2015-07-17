package com.wda.sdbc.base

trait Updatable[Connection, Key] {
  def update(key: Key): Update[Connection]
}

trait UpdatableMethods[Connection] {
  def updateIterator[Key](
    key: Key
  )(implicit updatable: Updatable[Connection, Key],
    connection: Connection
  ): Iterator[Long] = {
    updatable.update(key).iterator()
  }

  def update[Key](
    key: Key
  )(implicit updatable: Updatable[Connection, Key],
    connection: Connection
  ): Long = {
    updatable.update(key).update()
  }

}
