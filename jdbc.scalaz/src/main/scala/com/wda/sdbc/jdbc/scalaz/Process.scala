package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc
import scalaz.effect.IO
import scalaz.stream._

object Process {

  def apply[T](select: jdbc.Select[T])(
    implicit connection: Connection
  ): Process[IO, T] = {

    val acquire: IO[jdbc.Row] = IO {
      val statement = jdbc.prepare(
        queryText = select.queryText,
        parameterValues = select.parameterValues,
        parameterPositions = select.parameterPositions
      )
      new jdbc.Row(statement.executeQuery())
    }

    def release(row: jdbc.Row): IO[Unit] =
      IO(row.close())

    def step(row: jdbc.Row): IO[T] = IO {
      if (row.underlying.next()) {
        select.converter(row)
      } else {
        throw Cause.Terminated(Cause.End)
      }
    }

    io.resource[IO, jdbc.Row, T](acquire)(release)(step)
  }

  private case class TransactionResource(
    connection: Connection,
    row: jdbc.Row
  )

  def transaction[T](select: jdbc.Select[T])(
    implicit pool: jdbc.Pool
  ): Process[IO, T] = {

    val acquire: IO[TransactionResource] = IO {
      implicit val connection = pool.getConnection()
      connection.setAutoCommit(true)
      val statement = jdbc.prepare(
        queryText = select.queryText,
        parameterValues = select.parameterValues,
        parameterPositions = select.parameterPositions
      )
      TransactionResource(connection, new jdbc.Row(statement.executeQuery()))
    }

    def release(resource: TransactionResource): IO[Unit] = {
      IO(resource.row.close())
    }

    def step(resource: TransactionResource): IO[T] = {
      val TransactionResource(_, row) = resource
        IO {
          if (row.underlying.next()) {
            select.converter(row)
          } else {
            throw Cause.Terminated(Cause.End)
          }
        }
    }

    io.resource[IO, TransactionResource, T](acquire)(release)(step)

  }

}
