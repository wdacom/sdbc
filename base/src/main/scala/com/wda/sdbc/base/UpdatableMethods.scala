package com.wda.sdbc.base

import com.wda.sdbc.base

trait UpdatableMethods[Connection, Update <: base.Update[Connection]] {

  trait Updatable[Key] {
    def update(key: Key): Update
  }

  def updateIterator[Key](
    key: Key
  )(implicit updatable: Updatable[Key],
    connection: Connection
  ): Iterator[Long] = {
    updatable.update(key).iterator()
  }

  def update[Key](
    key: Key
  )(implicit updatable: Updatable[Key],
    connection: Connection
  ): Long = {
    updatable.update(key).update()
  }

}
