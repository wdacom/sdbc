package com.wda.sdbc
package cassandra

import com.datastax.driver.core.Session
import com.wda.sdbc.base.ParameterValueImplicits

abstract class Cassandra
  extends RowGetters
  with RowGetterImplicits
  with IndexImplicits
  with Row
  with ParameterValues
  with TupleParameterValues
  with TupleDataTypes
  with ParameterValueImplicits
  with TupleGetters {

  type Select[T] = cassandra.Select[T]

  val Select = cassandra.Select

  type Pool = Session

  type Selectable[Key, Value] = base.Selectable[Pool, Key, Value]

  type SelectableMethods[Value] = base.SelectableMethods[Pool, Value]

  type Updatable[Key] = base.Updatable[Pool, Key]

  type UpdatableMethods = base.UpdatableMethods[Pool]

  type Executable[Key] = base.Executable[Pool, Key]

  type ExecutableMethods = base.ExecutableMethods[Pool]

}
