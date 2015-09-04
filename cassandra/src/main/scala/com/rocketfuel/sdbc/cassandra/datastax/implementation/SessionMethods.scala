package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.datastax.driver.core.{Row => CRow}
import com.rocketfuel.sdbc.cassandra.datastax.Select

trait SessionMethods {
  self: Cassandra =>

  implicit class SessionMethods(pool: Session) {
    def iterator[T](
      queryText: String,
      parameters: (String, Option[ParameterValue])*
    )(implicit converter: CRow => T
    ): Iterator[T] = {
      Select[T](queryText).on(parameters: _*).iterator()(pool)
    }
  }

}
