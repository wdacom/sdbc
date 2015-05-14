package com.wda.sdbc.base

import scala.collection.immutable.Seq

trait Connection[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] extends Closable[Connection[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow]] {
  self: Select[UnderlyingConnection, PreparedStatement, UnderlyingResultSet, UnderlyingRow] =>

  def prepare(connection: UnderlyingConnection, queryText: String): PreparedStatement

  def iterator[T](
    queryText: String,
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
  )(implicit connection: UnderlyingConnection,
    converter: Row[UnderlyingRow] => T
  ): Iterator[T] = {
    implicit val isConnection = this
    Select[T](queryText).on(
      parameterValues: _*
    ).iterator()
  }

  def seq[T](
    queryText: String,
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
  )(implicit connection: UnderlyingConnection,
    converter: Row[UnderlyingRow] => T
  ): Seq[T] = {
    implicit val isConnection = this
    Select[T](queryText).on(
      parameterValues: _*
    ).seq()
  }

  def option[T](
    queryText: String,
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
  )(implicit connection: UnderlyingConnection,
    converter: Row[UnderlyingRow] => T
  ): Option[T] = {
    implicit val isConnection = this
    Select[T](queryText).on(
      parameterValues: _*
    ).option()
  }

  def single[T](
    queryText: String,
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
  )(implicit connection: UnderlyingConnection,
    converter: Row[UnderlyingRow] => T
  ): T = {
    implicit val isConnection = this
    Select[T](queryText).on(
      parameterValues: _*
    ).single()
  }

  def update(
    queryText: String,
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
  )(implicit connection: UnderlyingConnection
  ): Int = {
    implicit val isConnection = this
    Update(queryText).on(
      parameterValues: _*
    ).update()
  }

  def largeUpdate(
    queryText: String,
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
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
    parameterValues: (String, Option[ParameterValue[_, PreparedStatement]])*
  )(implicit connection: UnderlyingConnection
  ): Unit = {
    implicit val isConnection = this
    Update(queryText).on(
      parameterValues: _*
    ).execute()
  }

  def execute(preparedStatement: PreparedStatement): Unit
}
