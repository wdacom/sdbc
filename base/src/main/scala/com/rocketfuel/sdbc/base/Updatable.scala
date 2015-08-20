package com.rocketfuel.sdbc.base

import com.rocketfuel.sdbc.base

trait Updatable[Key, Connection, Update <: base.Update[Connection]] {
  def update(key: Key): Update
}

trait UpdatableMethods[Connection, Update <: base.Update[Connection]] {

  def updateIterator[Key](
    key: Key
  )(implicit updatable: Updatable[Key, Connection, Update],
    connection: Connection
  ): Iterator[Long] = {
    updatable.update(key).iterator()
  }

  def update[Key](
    key: Key
  )(implicit updatable: Updatable[Key, Connection, Update],
    connection: Connection
  ): Long = {
    updatable.update(key).update()
  }

}
