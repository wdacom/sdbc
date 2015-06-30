package com.wda.sdbc.cassandra

import com.datastax.driver.core.Session
import com.wda.sdbc.base
import com.wda.sdbc.base.ParameterValueImplicits

abstract class Cassandra
  extends RowGetters
  with IndexImplicits
  with Row
  with ParameterValues
  with TupleParameterValues
  with TupleDataTypes
  with ParameterValueImplicits
  with TupleGetters {

  type Selectable[Key, Value] = base.Selectable[Session, Key, Value]

  type SelectableMethods[Value] = base.SelectableMethods[Session, Value]

}
