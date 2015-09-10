package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.sql.Connection
import com.rocketfuel.sdbc.base._

case class QSeq[T](
  value: Seq[Option[ParameterValue]],
  typeName:  String
) {
  def asJavaArray: Array[AnyRef] = {
    QSeq.toJavaArray(this)
  }

  def asJdbcArray(connection: Connection): java.sql.Array = {
    connection.createArrayOf(typeName, asJavaArray)
  }
}

object QSeq extends ToParameter {

  override val toParameter: PartialFunction[Any, Any] = {
    case s: QSeq[_] => s
  }

  private def toJavaArray(q: QSeq[_]): Array[AnyRef] = {
    q.value.map(_.map {
      case ParameterValue(innerSeq: QSeq[_]) =>
        toJavaArray(innerSeq)
      case ParameterValue(otherwise) =>
        box(otherwise)
    }.orNull).toArray
  }

}
