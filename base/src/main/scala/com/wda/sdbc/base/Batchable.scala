package com.wda.sdbc.base

import com.wda.sdbc.base

trait Batchable[Key, Connection, Batch <: base.Batch[Connection]] {
  def batch(key: Key): Batch
}

trait BatchableMethods[Connection, Batch <: base.Batch[Connection]] {

  def batchIterator[Key](
    key: Key
  )(implicit batchable: Batchable[Key, Connection, Batch],
    connection: Connection
  ): Iterator[Long] = {
    batchable.batch(key).iterator()
  }

}
