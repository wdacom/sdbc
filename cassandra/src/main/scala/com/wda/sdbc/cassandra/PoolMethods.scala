package com.wda.sdbc.cassandra

trait PoolMethods {
  self: Cassandra =>

  implicit class PoolMethods(pool: Pool) {
    def iterator[T](
      queryText: String,
      parameters: (String, Option[ParameterValue[_]])*
    )(implicit converter: Row => T
    ): Iterator[T] = {
      Select[T](queryText).on(parameters: _*).iterator()(pool)
    }
  }

}
