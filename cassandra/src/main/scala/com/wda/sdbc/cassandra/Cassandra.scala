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
  with PoolMethods {

  type ParameterValue[+T] = cassandra.ParameterValue[T]

  type ParameterList = cassandra.ParameterList

  val Select = cassandra.Select

  val Execute = cassandra.Execute

  type Cluster = core.Cluster

  type Session = core.Session

  type Selectable[Key, Value] = cassandra.Selectable[Key, Value]

  type Executable[Key] = cassandra.Executable[Key]

}
