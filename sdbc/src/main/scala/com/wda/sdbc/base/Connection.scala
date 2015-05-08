package com.wda.sdbc.base

import scala.collection.immutable.Seq

trait Connection {
  self: AbstractQuery with Row with ParameterValue with Select with Update =>

  protected type UnderlyingConnection

  protected val isConnection: Connection

  protected val isClosableConnection: Closable[UnderlyingConnection]

  protected type PreparedStatement

  protected val isClosablePreparedStatement: Closable[PreparedStatement]

  protected trait Connection extends Closable[Connection] {
    def prepare(connection: UnderlyingConnection, queryText: String): PreparedStatement

    def iterator[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection,
      converter: Row => T
    ): Iterator[T] = {
      implicit val isConnection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).iterator()
    }

    def seq[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection,
      converter: Row => T
    ): Seq[T] = {
      implicit val isConnection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).seq()
    }

    def option[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection,
      converter: Row => T
    ): Option[T] = {
      implicit val isConnection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).option()
    }

    def single[T](
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection,
      converter: Row => T
    ): T = {
      implicit val isConnection = this
      Select[T](queryText).on(
        parameterValues: _*
      ).single()
    }

    def update(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection
    ): Int = {
      implicit val isConnection = this
      Update(queryText).on(
        parameterValues: _*
      ).update()
    }

    def largeUpdate(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection,
      isConnection: Connection
    ): Long = {
      implicit val isConnection = this
      Update(queryText).on(
        parameterValues: _*
      ).largeUpdate()
    }

    def execute(
      queryText: String,
      parameterValues: (String, Option[ParameterValue[_]])*
    )(implicit connection: UnderlyingConnection,
      isExecutable: Executable
    ): Unit = {
      implicit val isConnection = this
      Update(queryText).on(
        parameterValues: _*
      ).execute()
    }
  }

}
