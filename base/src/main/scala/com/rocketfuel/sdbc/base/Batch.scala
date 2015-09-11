package com.rocketfuel.sdbc.base

/**
 * A Batch is a Select where each value in the iterator is the number of
 * rows updated.
 * @tparam Connection
 */
abstract class Batch[Connection] extends Select[Connection, Long] {
  self: Logging =>

}
