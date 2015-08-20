package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.jdbc
import com.zaxxer.hikari.HikariDataSource

abstract class DBMS
  extends IndexImplicits
  with HikariImplicits
  with OptionParameter
  with GetterImplicits
  with UpdaterImplicits
  with base.BatchableMethods[java.sql.Connection, Batch]
  with base.UpdatableMethods[java.sql.Connection, Update]
  with base.SelectableMethods[java.sql.Connection, Select]
  with base.ExecutableMethods[java.sql.Connection, Execute]{

  type Row = jdbc.Row

  type MutableRow = jdbc.MutableRow

  type ParameterValue[+T] = jdbc.ParameterValue[T]

  type ParameterList = jdbc.ParameterList

  type Batchable[Key] = jdbc.Batchable[Key]

  override def batchIterator[Key](
    key: Key
  )(implicit batchable: Batchable[Key],
    connection: Connection
  ): Iterator[Long] = {
    jdbc.batchIterator[Key](key)
  }

  type Executable[Key] = jdbc.Executable[Key]

  override def execute[Key](
    key: Key
  )(implicit ev: Executable[Key],
    connection: Connection
  ): Unit = {
    jdbc.execute[Key](key)
  }

  type Selectable[Key, Value] = jdbc.Selectable[Key, Value]

  override def iterator[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Key, Value],
    connection: Connection
  ): Iterator[Value] = {
    jdbc.iterator[Key, Value](key)
  }

  override def option[Key, Value](
    key: Key
  )(implicit selectable: Selectable[Key, Value],
    connection: Connection
  ): Option[Value] = {
    jdbc.option[Key, Value](key)
  }

  type Updatable[Key] = jdbc.Updatable[Key]

  override def updateIterator[Key](
    key: Key
  )(implicit updatable: Updatable[Key],
    connection: Connection
  ): Iterator[Long] = {
    jdbc.updateIterator[Key](key)
  }

  override def update[Key](
    key: Key
  )(implicit updatable: Updatable[Key],
    connection: Connection
  ): Long = {
    jdbc.update[Key](key)
  }

  type Select[T] = jdbc.Select[T]

  val Select = jdbc.Select

  type Update = jdbc.Update

  val Update = jdbc.Update

  type Batch = jdbc.Batch

  val Batch = jdbc.Batch

  type Execute = jdbc.Execute

  val Execute = jdbc.Execute

  type Pool = jdbc.Pool

  val Pool = jdbc.Pool

  type Connection = jdbc.Connection

  implicit def PoolToHikariPool(pool: Pool): HikariDataSource = {
    pool.underlying
  }

  implicit class ConnectionMethods(connection: Connection) {
    def iterator[T](
      queryText: String,
      parameters: (String, Option[ParameterValue[_]])*
    )(implicit converter: Row => T
    ): Iterator[T] = {
      Select[T](queryText).on(parameters: _*).iterator()(connection)
    }

    def iteratorForUpdate(
      queryText: String,
      parameters: (String, Option[ParameterValue[_]])*
    ): Iterator[MutableRow] = {
      SelectForUpdate(queryText).on(parameters: _*).iterator()(connection)
    }

    def update(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Long = {
      Update(queryText).on(parameterValues: _*).update()(connection)
    }

    def execute(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    ): Unit = {
      Execute(queryText).on(parameterValues: _*).execute()(connection)
    }
  }

  /**
   * Class name for the DataSource class.
   */
  def dataSourceClassName: String

  /**
   * Class name for the JDBC driver class.
   */
  def driverClassName: String

  def jdbcSchemes: Set[String]

  /**
   * The result of getMetaData.getDatabaseProductName
   */
  def productName: String

  /**
   * If the JDBC driver supports the .isValid() method.
   */
  def supportsIsValid: Boolean

  /**
   * Perform any connection initialization that should be done when a connection
   * is created. EG add a type mapping.
   *
   * By default this method does nothing.
   * @param connection
   */
  def initializeConnection(connection: java.sql.Connection): Unit = {

  }

  register(this)

}
