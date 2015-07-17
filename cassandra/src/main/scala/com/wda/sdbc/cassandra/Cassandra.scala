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

  type Connection = java.sql.Connection

  type Pool = Session

  type Selectable[Key, Value] = base.Selectable[Pool, Key, Value]

  type SelectableMethods[Value] = base.SelectableMethods[Pool, Value]

  type Updatable[Key] = base.Updatable[Connection, Key]

  type UpdatableMethods = base.UpdatableMethods[Connection]

  type Executable[Key] = base.Executable[Connection, Key]

  type ExecutableMethods = base.ExecutableMethods[Connection]

}
