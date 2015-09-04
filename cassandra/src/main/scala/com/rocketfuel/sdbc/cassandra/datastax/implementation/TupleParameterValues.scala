package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.datastax.driver.core.{BoundStatement, TupleType}
import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.cassandra.datastax.implementation.TupleParameterValues.boxOption
import shapeless._
import shapeless.poly._
import shapeless.syntax.std.tuple._

trait TupleParameterValues {

  object toSome extends (Id ~> Option) {
    override def apply[T](f: Id[T]): Option[T] = {
      Some(f)
    }
  }

  implicit def Tuple0ToParameterValue(value: Unit): ParameterValue = {
    ParameterValue(TupleType.of().newValue())
  }

  implicit def TupleOption1ToParameterValue[T0](
    value: (Option[T0])
  )(implicit
    dt0: TupleDataType[T0]
  ): ParameterValue = {
    val tupleValue = TupleType.of(dt0.dataType).newValue(boxOption(value))
    ParameterValue(tupleValue)
  }

  implicit def Tuple1ToParameterValue[T0](
    value: (T0)
  )(implicit
    dt0: TupleDataType[T0]
  ): ParameterValue = {
    ParameterValue(Some(value))
  }

  implicit def TupleOption2ToParameterValue[T0, T1](
    value: (Option[T0], Option[T1])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ): ParameterValue = {
    val tupleValue = TupleType.of(dt0.dataType, dt1.dataType).newValue(value.toList.map(boxOption): _*)
    ParameterValue(tupleValue)
  }

  implicit def Tuple2ToParameterValue[T0, T1](
    value: (T0, T1)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ): ParameterValue = {
    TupleOption2ToParameterValue[T0, T1](value.map(toSome))
  }

}

object TupleParameterValues {
  def boxOption(v: Option[Any]): AnyRef = {
    v.map(base.box).orNull
  }

  def createInstance(arity: Int): String = {
    val builder = new StringBuilder()

    def enumParameters() = {
      for (i <- 0 until arity) {
        builder.append(s"T$i")
        if (i != arity - 1) builder.append(", ")
      }
    }

    def enumOptionParameters() = {
      for (i <- 0 until arity) {
        builder.append(s"Option[T$i]")
        if (i != arity - 1) builder.append(", ")
      }
    }

    def enumImplicitArgs() = {
      for (i <- 0 until arity) {
        builder.append(s"  dt$i: TupleDataType[T$i]")
        if (i != arity - 1) builder.append(",\n")
      }
    }

    builder.append(s"implicit def TupleOption${arity}ToParameterValue[")

    enumParameters()

    builder.append("](\n  value: (")

    enumOptionParameters()

    builder.append(s")\n)(implicit\n")

    enumImplicitArgs()

    builder.append(s"\n): ParameterValue = {\n  ParameterValue(value)\n}")

    builder.append(s"\n\nimplicit def Tuple${arity}ToParameterValue[")

    enumParameters()

    builder.append("](\n  value: (")

    enumParameters()

    builder.append(s")\n)(implicit\n")

    enumImplicitArgs()

    builder.append(s"\n): ParameterValue = {\n  value.map(toSome)\n}")

    builder.toString()
  }

  def createInstances(maxArity: Int): String = {
    val builder = new StringBuilder()

    for (arity <- 0 to maxArity) {
      builder.append(createInstance(arity))
      builder.append("\n\n")
    }

    builder.toString()
  }

}
