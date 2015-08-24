package com.rocketfuel.sdbc.base.jdbc

import java.sql.PreparedStatement

import scala.reflect.runtime.universe._

trait ArrayParameter {
  self: DBMS =>

  def typeName[T](implicit tag: TypeTag[T]): String = {
    typeName(tag.tpe)
  }

  def typeName(tpe: Type): String

  def innerTypeName(tpe: Type): String = {
    val innerType = tpe.dealias.typeArgs.head.dealias
    typeName(innerType)
  }

  case class QArray[+T](
    override val value: Seq[Option[ParameterValue[T]]]
  )(implicit t: TypeTag[T]
  ) extends ParameterValue[Seq[Option[ParameterValue[T]]]] {
    /**
     * Get the values of this array in a form suitable for use
     * with Java methods that expect an Array.
     * @return
     */
    def asJava: Array[AnyRef] = {
      value.map(_.map(v => QArray.box(v.value)).orNull).toArray
    }

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      val array = preparedStatement.getConnection.createArrayOf(typeName[T], asJava)
      preparedStatement.setArray(parameterIndex, array)
    }

  }

  object QArray {
    def box(v: Any): AnyRef = {
      v match {
        case a: AnyRef => a
        case b: Boolean => Boolean.box(b)
        case b: Byte => Byte.box(b)
        case c: Char => Char.box(c)
        case s: Short => Short.box(s)
        case i: Int => Int.box(i)
        case l: Long => Long.box(l)
        case f: Float => Float.box(f)
        case d: Double => Double.box(d)
      }
    }
  }

  implicit def SeqToOptionParameterValue[T, S](
    v: Seq[T]
  )(implicit conversion: T => ParameterValue[S],
    ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    Some(QArray(v.map(conversion andThen Some.apply)))
  }

  implicit def OptionSeqToOptionParameterValue[T, S](
    vOpt: Option[Seq[T]]
  )(implicit conversion: T => ParameterValue[S], ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    vOpt.map(v => QArray(v.map(conversion andThen Some.apply)))
  }

  implicit def SeqOptionToOptionParameterValue[T, S](
    v: Seq[Option[T]]
  )(implicit conversion: T => ParameterValue[S],
    ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    Some(QArray(v.map(_.map(conversion))))
  }

  implicit def OptionOptionSeqToOptionParameterValue[T, S](
    vOpt: Option[Seq[Option[T]]]
  )(implicit conversion: T => ParameterValue[S],
    ttag: TypeTag[S]
  ): Option[ParameterValue[Seq[Option[ParameterValue[S]]]]] = {
    vOpt.map(v => QArray(v.map(_.map(conversion))))
  }

  implicit def GetterToSeqOptionGetter[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = {
    (row: Row, ix: Index) =>
      Option(row.getArray(ix(row))).map(_.getResultSet().iterator().map(_.get[T](IntIndex(1))).toVector)
  }

  implicit def GetterToSeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = {
    (row: Row, ix: Index) =>
      GetterToSeqOptionGetter(getter)(row, ix).map(_.map(_.get))
  }

}
