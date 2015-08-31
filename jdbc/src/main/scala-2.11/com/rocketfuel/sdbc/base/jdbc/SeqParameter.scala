package com.rocketfuel.sdbc.base.jdbc

import java.sql.PreparedStatement

import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.ToParameter

import scala.reflect.runtime.universe._

trait SeqParameter {
  self: DBMS =>

  def typeName[T](implicit tag: TypeTag[T]): String = {
    typeName(tag.tpe)
  }

  def typeName(tpe: Type): String

  def innerTypeName(tpe: Type): String = {
    val innerType = tpe.dealias.typeArgs.head.dealias
    typeName(innerType)
  }

  case class QSeq[T](
    value: Seq[Option[ParameterValue]]
  )(implicit val ttag: TypeTag[T]
  ) {
    /**
     * Get the values of this array in a form suitable for use
     * with Java methods that expect an Array.
     * @return
     */
    def asJava: Array[AnyRef] = {
      value.map(_.map(v => base.box(v.value)).orNull).toArray
    }
  }

  object QSeq extends ToParameter with QSeqImplicits {
    override val toParameter: PartialFunction[Any, Any] = {
      case s: QSeq[_] => s
    }
  }

  trait QSeqImplicits {
    implicit def QSeqIsParameter: IsParameter[QSeq[_]] = new IsParameter[QSeq[_]] {
      override def set(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: QSeq[_]): Unit = {
        val array = preparedStatement.getConnection.createArrayOf(typeName(parameter.ttag), parameter.asJava)
        preparedStatement.setArray(parameterIndex, array)
      }
    }
  }

  implicit def SeqToOptionParameterValue[T, S](
    v: Seq[T]
  )(implicit conversion: T => ParameterValue,
    ttag: TypeTag[S]
  ): ParameterValue = {
    base.ParameterValue(QSeq(v.map(conversion andThen Some.apply)))
  }

  implicit def SeqOptionToOptionParameterValue[T, S](
    v: Seq[Option[T]]
  )(implicit conversion: T => ParameterValue,
    ttag: TypeTag[S]
  ): ParameterValue = {
    ParameterValue(QSeq(v.map(_.map(conversion))))
  }

  implicit def GetterToSeqOptionGetter[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = {
    (row: Row, ix: Index) =>
      for {
        a <- Option(row.getArray(ix(row)))
      } yield {
        val arrayIterator = a.getResultSet().iterator()
        arrayIterator.map(_.get[T](IntIndex(1))).toVector
      }
  }

  implicit def GetterToSeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = {
    (row: Row, ix: Index) =>
      GetterToSeqOptionGetter(getter)(row, ix).map(_.map(_.get))
  }

}
