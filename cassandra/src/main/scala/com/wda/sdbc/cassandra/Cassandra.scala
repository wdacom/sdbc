package com.wda.sdbc
package cassandra

import com.datastax.driver.core
import com.wda.sdbc.base.ParameterValueImplicits

abstract class Cassandra
  extends RowGetters
  with RowGetterImplicits
  with IndexImplicits
  with RowMethods
  with ParameterValues
  with TupleParameterValues
  with TupleDataTypes
  with ParameterValueImplicits
  with TupleGetters
  with PoolMethods
  with SelectableMethods
  with ExecutableMethods {

  val Select = cassandra.Select

  val Execute = cassandra.Execute

  type Cluster = core.Cluster

  type Session = core.Session

}
