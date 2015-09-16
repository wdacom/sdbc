package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.rocketfuel.sdbc.cassandra.datastax.QueryOptions

private[sdbc] trait HasQueryOptions {
  def queryOptions: QueryOptions
}
