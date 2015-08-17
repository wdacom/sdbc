package com.wda.sdbc.cassandra

import com.wda.sdbc.cassandra
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
  with ExecutableMethods
  with SelectableMethods {

  type ParameterValue[+T] = cassandra.ParameterValue[T]

  type ParameterList = cassandra.ParameterList

  type Select[T] = cassandra.Select[T]

  val Select = cassandra.Select

  type Execute = cassandra.Execute

  val Execute = cassandra.Execute

  type Cluster = core.Cluster

  type Session = core.Session

  type Selectable[Key, Value] = cassandra.Selectable[Key, Value]

  type Executable[Key] = cassandra.Executable[Key]

}
