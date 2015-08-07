package com.wda.sdbc.base

import com.wda.sdbc.base

trait Updatable[Connection, Key, U <: Update[Connection]] {
  def update(key: Key): Update[Connection]
}

trait UpdatableMethods[Connection] {

  type Update <: base.Update[Connection]

  def updateIterator[Key](
    key: Key
  )(implicit updatable: Updatable[Connection, Key, Update],
    connection: Connection
  ): Iterator[Long] = {
    updatable.update(key).iterator()
  }

  def update[Key](
    key: Key
  )(implicit updatable: Updatable[Connection, Key, Update],
    connection: Connection
  ): Long = {
    updatable.update(key).update()
  }

}
