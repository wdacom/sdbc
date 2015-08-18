package com.wda.sdbc.cassandra.datastax.implementation

import com.wda.sdbc.cassandra.datastax.QueryOptions

trait HasQueryOptions {
  def queryOptions: QueryOptions
}
