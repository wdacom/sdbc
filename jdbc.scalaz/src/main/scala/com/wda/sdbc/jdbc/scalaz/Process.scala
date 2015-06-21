package com.wda.sdbc.jdbc.scalaz

import java.sql.Connection

import com.wda.sdbc.jdbc
import scalaz.stream._
import scalaz.Applicative

object Process {

  def apply[F[_], T](
    select: jdbc.Select[T]
  )(implicit connection: Connection,
    ev: Applicative[F]
  ): Process[F, T] = {

    val acquire: F[jdbc.Row] = ev.point {
      val statement = jdbc.prepare(
        queryText = select.queryText,
        parameterValues = select.parameterValues,
        parameterPositions = select.parameterPositions
      )
      new jdbc.Row(statement.executeQuery())
    }

    def release(row: jdbc.Row): F[Unit] =
      ev.point(row.close())

    def step(row: jdbc.Row): F[T] = ev.point {
      if (row.underlying.next()) {
        select.converter(row)
      } else {
        throw Cause.Terminated(Cause.End)
      }
    }

    io.resource[F, jdbc.Row, T](acquire)(release)(step)
  }

  private case class TransactionResource(
    connection: Connection,
    row: jdbc.Row
  )

  def transaction[F[_], T](
    select: jdbc.Select[T]
  )(implicit pool: jdbc.Pool,
    ev: Applicative[F]
  ): Process[F, T] = {

    val acquire: F[TransactionResource] = ev.point {
      implicit val connection = pool.getConnection()
      connection.setAutoCommit(true)
      val statement = jdbc.prepare(
        queryText = select.queryText,
        parameterValues = select.parameterValues,
        parameterPositions = select.parameterPositions
      )
      TransactionResource(connection, new jdbc.Row(statement.executeQuery()))
    }

    def release(resource: TransactionResource): F[Unit] = {
      ev.point(resource.row.close())
    }

    def step(resource: TransactionResource): F[T] = {
      val TransactionResource(_, row) = resource
        ev.point {
          if (row.underlying.next()) {
            select.converter(row)
          } else {
            throw Cause.Terminated(Cause.End)
          }
        }
    }

    io.resource[F, TransactionResource, T](acquire)(release)(step)

  }

}
