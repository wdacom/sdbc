package com.wda.sdbc.base

import com.wda.sdbc.base

trait BatchableMethods[Connection, Batch <: base.Batch[Connection]] {

  trait Batchable[Key] {
    def batch(key: Key): Batch
  }

  def batchIterator[Key](
    key: Key
  )(implicit batchable: Batchable[Key],
    connection: Connection
  ): Iterator[Long] = {
    batchable.batch(key).iterator()
  }

}
