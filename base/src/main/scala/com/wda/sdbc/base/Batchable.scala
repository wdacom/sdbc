package com.wda.sdbc.base

import com.wda.sdbc.base

trait Batchable[Connection, Key, B <: Batch[Connection]] {
  def batch(key: Key): Batch[Connection]
}

trait BatchableMethods[Connection] {

  type Batch <: base.Batch[Connection]

  def batchIterator[Key](
    key: Key
  )(implicit batchable: Batchable[Connection, Key, Batch],
    connection: Connection
  ): Iterator[Long] = {
    batchable.batch(key).iterator()
  }

}
