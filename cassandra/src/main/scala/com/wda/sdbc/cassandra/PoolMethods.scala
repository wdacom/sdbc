package com.wda.sdbc.cassandra

import com.datastax.driver.core.{Row => CRow}

trait PoolMethods {
  self: Cassandra =>

  implicit class PoolMethods(pool: Pool) {
    def iterator[T](
      queryText: String,
      parameters: (String, Option[ParameterValue[_]])*
    )(implicit converter: CRow => T
    ): Iterator[T] = {
      Select[T](queryText).on(parameters: _*).iterator()(pool)
    }
  }

}
