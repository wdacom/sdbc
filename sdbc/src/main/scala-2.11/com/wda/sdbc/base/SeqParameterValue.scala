package com.wda.sdbc.base

import java.sql.PreparedStatement

import scala.reflect.runtime.universe._

trait SeqParameterValue {
  self: Row with Getter with ParameterValue =>

  def typeName[T](implicit tag: TypeTag[T]): String

  case class QArray[T](
    override val value: Seq[Option[ParameterValue[T]]]
    )(implicit t: TypeTag[T]
    ) extends ParameterValue[Seq[Option[ParameterValue[T]]]] {
    /**
     * Get the values of this array in a form suitable for use
     * with Java methods that expect an Array.
     * @return
     */
    def asJava: Array[AnyRef] = {
      value.map(_.map(v => v.asJDBCObject).orNull).toArray
    }

    override def asJDBCObject: AnyRef = asJava

    override def set(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
      val array = preparedStatement.getConnection.createArrayOf(typeName[T], asJava)
      preparedStatement.setArray(parameterIndex, array)
    }

    override def update(row: Row, columnIndex: Int): Unit = {
      throw new NotImplementedError("Creating a JDBC array requires access to the Connection object, which Row doesn't have a pointer to.")
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

  implicit class RowSeqOps(row: Row) {

    def seq[T]()(implicit getter: Getter[T]): Seq[Option[T]] = {
      optionSeq[T].get
    }

    def seq[T](columnName: String)(implicit getter: Getter[T]): Seq[Option[T]] = {
      optionSeq[T](columnName).get
    }

    def seq[T](columnIndex: Int)(implicit getter: Getter[T]): Seq[Option[T]] = {
      optionSeq[T](columnIndex).get
    }

    def optionSeq[T](implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      optionSeq(1)
    }

    def optionSeq[T](columnName: String)(implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      optionSeq(row.columnIndexes(columnName))
    }

    def optionSeq[T](columnIndex: Int)(implicit getter: Getter[T]): Option[Seq[Option[T]]] = {
      Option(row.getArray(columnIndex)).map { array =>
        val arrayAsResultSet = array.getResultSet
        arrayAsResultSet.iterator().map { row =>
          row.option[T](2)(getter)
        }.toVector
      }
    }

  }

  implicit def GetterToSeqOptionGetter[T](implicit getter: Getter[T]): Getter[Seq[Option[T]]] = new Getter[Seq[Option[T]]] {
    override def apply(row: Row, columnIndex: Int): Option[Seq[Option[T]]] = {
      row.optionSeq[T](columnIndex)
    }
  }

  implicit def GetterToSeqGetter[T](implicit getter: Getter[T]): Getter[Seq[T]] = new Getter[Seq[T]] {
    override def apply(row: Row, columnIndex: Int): Option[Seq[T]] = {
      row.optionSeq[T](columnIndex).map(_.map(_.get))
    }
  }

}
