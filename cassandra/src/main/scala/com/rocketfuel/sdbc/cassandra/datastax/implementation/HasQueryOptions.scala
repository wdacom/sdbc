package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.rocketfuel.sdbc.cassandra.datastax.QueryOptions

trait HasQueryOptions {
  def queryOptions: QueryOptions
}
