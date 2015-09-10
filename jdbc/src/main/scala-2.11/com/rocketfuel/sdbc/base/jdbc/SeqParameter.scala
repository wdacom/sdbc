package com.rocketfuel.sdbc.base.jdbc

import java.sql.PreparedStatement
import com.rocketfuel.sdbc.base._
import scala.reflect.runtime.universe._

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

trait SeqParameter {
  self: BytesGetter =>

  def typeName[T](implicit tag: TypeTag[T]): String = {
    typeName(tag.tpe)
  }

  def typeName(tpe: Type): String

  def innerTypeName(tpe: Type): String = {
    val innerType = tpe.dealias.typeArgs.head.dealias
    typeName(innerType)
  }

  implicit def SeqOptionToOptionParameterValue[T](
    v: Seq[Option[T]]
  )(implicit conversion: T => ParameterValue,
    ttag: TypeTag[T]
  ): ParameterValue = {
    ParameterValue(QSeq(v.map(_.map(conversion)), typeName[T]))
  }

  implicit def SeqToOptionParameterValue[T](
    v: Seq[T]
  )(implicit conversion: T => ParameterValue,
    ttag: TypeTag[T]
  ): ParameterValue = {
    v.map(Some.apply)
  }

  implicit def GetterToSeqOptionGetter[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = {
    (row: Row, ix: Index) =>
      for {
        a <- Option(row.getArray(ix(row)))
      } yield {
        val arrayIterator = a.getResultSet().iterator()
        val arrayValues = for {
          arrayRow <- arrayIterator
        } yield {
          arrayRow.get[T](IntIndex(1))
        }
        arrayValues.toVector
      }
  }

  implicit def GetterToSeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = {
    (row: Row, ix: Index) =>
      GetterToSeqOptionGetter(getter)(row, ix).map(_.map(_.get))
  }

  implicit val QSeqIsParameter: IsParameter[QSeq[_]] = new IsParameter[QSeq[_]] {
    override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: QSeq[_]): Unit = {
      preparedStatement.setArray(parameterIndex, parameter.asJdbcArray(preparedStatement.getConnection))
    }
  }

  //Override what would be the inferred Seq[Byte] getter, because you can't use ResultSet#getArray
  //to get the bytes.
  implicit val SeqByteGetter = new Getter[Seq[Byte]] {
    override def apply(row: Row, ix: Index): Option[Seq[Byte]] = {
      ArrayByteGetter(row, ix).map(_.toSeq)
    }
  }

}

trait SeqParameterUpdaters {
  self: SeqParameter with BytesGetter =>

  implicit def SetterToSeqOptionUpdater[T](implicit conversion: T => ParameterValue, ttag: TypeTag[T]): Updater[Seq[Option[T]]] = {
    new Updater[Seq[Option[T]]] {
      override def update(row: UpdatableRow, columnIndex: Int, x: Seq[Option[T]]): Unit = {
        val asParameterValue: ParameterValue = x
        val ParameterValue(q: QSeq[_]) = asParameterValue
        row.updateArray(columnIndex, q.asJdbcArray(row.getStatement.getConnection))
      }
    }
  }

  implicit def SetterToSeqUpdater[T](implicit conversion: T => ParameterValue, ttag: TypeTag[T]): Updater[Seq[T]] = {
    new Updater[Seq[T]] {
      val optionUpdater = implicitly[Updater[Seq[Option[T]]]]
      override def update(row: UpdatableRow, columnIndex: Int, x: Seq[T]): Unit = {
        val optValue = x.map(Some.apply)
        optionUpdater.update(row, columnIndex, optValue)
      }
    }
  }

}
