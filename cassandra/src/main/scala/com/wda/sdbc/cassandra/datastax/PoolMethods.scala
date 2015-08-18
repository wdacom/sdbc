package com.wda.sdbc.cassandra.datastax

import com.datastax.driver.core.{Row => CRow}

trait PoolMethods {
  self: Datastax =>

  implicit class PoolMethods(pool: Session) {
    def iterator[T](
      queryText: String,
      parameters: (String, Option[ParameterValue[_]])*
    )(implicit converter: CRow => T
    ): Iterator[T] = {
      Select[T](queryText).on(parameters: _*).iterator()(pool)
    }
  }

}
