package com.wda.sdbc.cassandra.datastax

import com.wda.sdbc.cassandra.datastax
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

  type ParameterValue[+T] = datastax.ParameterValue[T]

  type ParameterList = datastax.ParameterList

  type Select[T] = datastax.Select[T]

  val Select = datastax.Select

  type Execute = datastax.Execute

  val Execute = datastax.Execute

  type Cluster = core.Cluster

  type Session = core.Session

  type Selectable[Key, Value] = datastax.Selectable[Key, Value]

  type Executable[Key] = datastax.Executable[Key]

}

case object Cassandra extends Cassandra
