package com.wda.sdbc.cassandra.datastax.implementation

import com.datastax.driver.core.{BoundStatement, TupleType}
import com.wda.sdbc.cassandra.datastax.implementation.TupleParameterValues.boxOption
import shapeless._
import shapeless.poly._
import shapeless.syntax.std.tuple._

trait TupleParameterValues {

  object toSome extends (Id ~> Option) {
    override def apply[T](f: Id[T]): Option[T] = {
      Some(f)
    }
  }

  case class Tuple0Parameter(
    value: Unit
  ) extends ParameterValue[Unit] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of().newValue()
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def Tuple0ToParameterValue(
    value: Unit
  ): Tuple0Parameter = {
    Tuple0Parameter(value)
  }

  case class Tuple1Parameter[T0](
    value: (Option[T0])
  )(implicit
    dt0: TupleDataType[T0]
  ) extends ParameterValue[(Option[T0])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType).newValue(boxOption(value))
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption1ToParameterValue[T0](
    value: (Option[T0])
  )(implicit
    dt0: TupleDataType[T0]
  ): Tuple1Parameter[T0] = {
    Tuple1Parameter[T0](value)
  }

  implicit def Tuple1ToParameterValue[T0](
    value: (T0)
  )(implicit
    dt0: TupleDataType[T0]
  ): Tuple1Parameter[T0] = {
    Some(value)
  }

  case class Tuple2Parameter[T0, T1](
    value: (Option[T0], Option[T1])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ) extends ParameterValue[(Option[T0], Option[T1])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption2ToParameterValue[T0, T1](
    value: (Option[T0], Option[T1])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ): Tuple2Parameter[T0, T1] = {
    Tuple2Parameter[T0, T1](value)
  }

  implicit def Tuple2ToParameterValue[T0, T1](
    value: (T0, T1)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ): Tuple2Parameter[T0, T1] = {
    value.map(toSome)
  }

  case class Tuple3Parameter[T0, T1, T2](
    value: (Option[T0], Option[T1], Option[T2])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption3ToParameterValue[T0, T1, T2](
    value: (Option[T0], Option[T1], Option[T2])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2]
  ): Tuple3Parameter[T0, T1, T2] = {
    Tuple3Parameter[T0, T1, T2](value)
  }

  implicit def Tuple3ToParameterValue[T0, T1, T2](
    value: (T0, T1, T2)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2]
  ): Tuple3Parameter[T0, T1, T2] = {
    value.map(toSome)
  }

  case class Tuple4Parameter[T0, T1, T2, T3](
    value: (Option[T0], Option[T1], Option[T2], Option[T3])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption4ToParameterValue[T0, T1, T2, T3](
    value: (Option[T0], Option[T1], Option[T2], Option[T3])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3]
  ): Tuple4Parameter[T0, T1, T2, T3] = {
    Tuple4Parameter[T0, T1, T2, T3](value)
  }

  implicit def Tuple4ToParameterValue[T0, T1, T2, T3](
    value: (T0, T1, T2, T3)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3]
  ): Tuple4Parameter[T0, T1, T2, T3] = {
    value.map(toSome)
  }

  case class Tuple5Parameter[T0, T1, T2, T3, T4](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption5ToParameterValue[T0, T1, T2, T3, T4](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4]
  ): Tuple5Parameter[T0, T1, T2, T3, T4] = {
    Tuple5Parameter[T0, T1, T2, T3, T4](value)
  }

  implicit def Tuple5ToParameterValue[T0, T1, T2, T3, T4](
    value: (T0, T1, T2, T3, T4)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4]
  ): Tuple5Parameter[T0, T1, T2, T3, T4] = {
    value.map(toSome)
  }

  case class Tuple6Parameter[T0, T1, T2, T3, T4, T5](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption6ToParameterValue[T0, T1, T2, T3, T4, T5](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5]
  ): Tuple6Parameter[T0, T1, T2, T3, T4, T5] = {
    Tuple6Parameter[T0, T1, T2, T3, T4, T5](value)
  }

  implicit def Tuple6ToParameterValue[T0, T1, T2, T3, T4, T5](
    value: (T0, T1, T2, T3, T4, T5)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5]
  ): Tuple6Parameter[T0, T1, T2, T3, T4, T5] = {
    value.map(toSome)
  }

  case class Tuple7Parameter[T0, T1, T2, T3, T4, T5, T6](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption7ToParameterValue[T0, T1, T2, T3, T4, T5, T6](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6]
  ): Tuple7Parameter[T0, T1, T2, T3, T4, T5, T6] = {
    Tuple7Parameter[T0, T1, T2, T3, T4, T5, T6](value)
  }

  implicit def Tuple7ToParameterValue[T0, T1, T2, T3, T4, T5, T6](
    value: (T0, T1, T2, T3, T4, T5, T6)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6]
  ): Tuple7Parameter[T0, T1, T2, T3, T4, T5, T6] = {
    value.map(toSome)
  }

  case class Tuple8Parameter[T0, T1, T2, T3, T4, T5, T6, T7](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption8ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7]
  ): Tuple8Parameter[T0, T1, T2, T3, T4, T5, T6, T7] = {
    Tuple8Parameter[T0, T1, T2, T3, T4, T5, T6, T7](value)
  }

  implicit def Tuple8ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7](
    value: (T0, T1, T2, T3, T4, T5, T6, T7)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7]
  ): Tuple8Parameter[T0, T1, T2, T3, T4, T5, T6, T7] = {
    value.map(toSome)
  }

  case class Tuple9Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption9ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8]
  ): Tuple9Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8] = {
    Tuple9Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8](value)
  }

  implicit def Tuple9ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8]
  ): Tuple9Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8] = {
    value.map(toSome)
  }

  case class Tuple10Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption10ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9]
  ): Tuple10Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9] = {
    Tuple10Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](value)
  }

  implicit def Tuple10ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9]
  ): Tuple10Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9] = {
    value.map(toSome)
  }

  case class Tuple11Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption11ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10]
  ): Tuple11Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = {
    Tuple11Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](value)
  }

  implicit def Tuple11ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10]
  ): Tuple11Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10] = {
    value.map(toSome)
  }

  case class Tuple12Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption12ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11]
  ): Tuple12Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = {
    Tuple12Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](value)
  }

  implicit def Tuple12ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11]
  ): Tuple12Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11] = {
    value.map(toSome)
  }

  case class Tuple13Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption13ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12]
  ): Tuple13Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = {
    Tuple13Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](value)
  }

  implicit def Tuple13ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12]
  ): Tuple13Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12] = {
    value.map(toSome)
  }

  case class Tuple14Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption14ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13]
  ): Tuple14Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = {
    Tuple14Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](value)
  }

  implicit def Tuple14ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13]
  ): Tuple14Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13] = {
    value.map(toSome)
  }

  case class Tuple15Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption15ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14]
  ): Tuple15Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = {
    Tuple15Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](value)
  }

  implicit def Tuple15ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14]
  ): Tuple15Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14] = {
    value.map(toSome)
  }

  case class Tuple16Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption16ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15]
  ): Tuple16Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = {
    Tuple16Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](value)
  }

  implicit def Tuple16ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15]
  ): Tuple16Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15] = {
    value.map(toSome)
  }

  case class Tuple17Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption17ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16]
  ): Tuple17Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = {
    Tuple17Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](value)
  }

  implicit def Tuple17ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16]
  ): Tuple17Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16] = {
    value.map(toSome)
  }

  case class Tuple18Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption18ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17]
  ): Tuple18Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = {
    Tuple18Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](value)
  }

  implicit def Tuple18ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17]
  ): Tuple18Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17] = {
    value.map(toSome)
  }

  case class Tuple19Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption19ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18]
  ): Tuple19Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = {
    Tuple19Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](value)
  }

  implicit def Tuple19ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18]
  ): Tuple19Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18] = {
    value.map(toSome)
  }

  case class Tuple20Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType, dt19.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption20ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19]
  ): Tuple20Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = {
    Tuple20Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](value)
  }

  implicit def Tuple20ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19]
  ): Tuple20Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19] = {
    value.map(toSome)
  }

  case class Tuple21Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19], Option[T20])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19],
    dt20: TupleDataType[T20]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19], Option[T20])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType, dt19.dataType, dt20.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption21ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19], Option[T20])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19],
    dt20: TupleDataType[T20]
  ): Tuple21Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = {
    Tuple21Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](value)
  }

  implicit def Tuple21ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19],
    dt20: TupleDataType[T20]
  ): Tuple21Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20] = {
    value.map(toSome)
  }

  case class Tuple22Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19], Option[T20], Option[T21])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19],
    dt20: TupleDataType[T20],
    dt21: TupleDataType[T21]
  ) extends ParameterValue[(Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19], Option[T20], Option[T21])] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType, dt19.dataType, dt20.dataType, dt21.dataType).newValue(value.toList.map(boxOption): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def TupleOption22ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](
    value: (Option[T0], Option[T1], Option[T2], Option[T3], Option[T4], Option[T5], Option[T6], Option[T7], Option[T8], Option[T9], Option[T10], Option[T11], Option[T12], Option[T13], Option[T14], Option[T15], Option[T16], Option[T17], Option[T18], Option[T19], Option[T20], Option[T21])
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19],
    dt20: TupleDataType[T20],
    dt21: TupleDataType[T21]
  ): Tuple22Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = {
    Tuple22Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](value)
  }

  implicit def Tuple22ToParameterValue[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](
    value: (T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6],
    dt7: TupleDataType[T7],
    dt8: TupleDataType[T8],
    dt9: TupleDataType[T9],
    dt10: TupleDataType[T10],
    dt11: TupleDataType[T11],
    dt12: TupleDataType[T12],
    dt13: TupleDataType[T13],
    dt14: TupleDataType[T14],
    dt15: TupleDataType[T15],
    dt16: TupleDataType[T16],
    dt17: TupleDataType[T17],
    dt18: TupleDataType[T18],
    dt19: TupleDataType[T19],
    dt20: TupleDataType[T20],
    dt21: TupleDataType[T21]
  ): Tuple22Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21] = {
    value.map(toSome)
  }

}

object TupleParameterValues {
  def boxOption(v: Option[Any]): AnyRef = {
    v.map(box).orNull
  }

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

    builder.append(s"case class Tuple${arity}Parameter[")

    enumParameters()

    builder.append("](\n  value: (")

    enumOptionParameters()

    builder.append(")\n)(implicit\n")

    enumImplicitArgs()

    builder.append("\n) extends ParameterValue[(")

    enumOptionParameters()

    builder.append(")] {\n  override def set(\n    statement: BoundStatement,\n    parameterIndex: Int\n  ): Unit = {\n    val tupleValue = TupleType.of(")

    for (i <- 0 until arity) {
      builder.append(s"dt$i.dataType")
      if (i != arity - 1) builder.append(", ")
    }

    builder.append(").newValue(value.toList.map(boxOption): _*)\n    statement.setTupleValue(parameterIndex, tupleValue)\n  }\n}\n\n")

    builder.append(s"implicit def TupleOption${arity}ToParameterValue[")

    enumParameters()

    builder.append("](\n  value: (")

    enumOptionParameters()

    builder.append(s")\n)(implicit\n")

    enumImplicitArgs()

    builder.append(s"\n): Tuple${arity}Parameter[")

    enumParameters()

    builder.append(s"] = {\n  Tuple${arity}Parameter[")

    enumParameters()

    builder.append("](value)\n}")

    builder.append(s"\n\nimplicit def Tuple${arity}ToParameterValue[")

    enumParameters()

    builder.append("](\n  value: (")

    enumParameters()

    builder.append(s")\n)(implicit\n")

    enumImplicitArgs()

    builder.append(s"\n): Tuple${arity}Parameter[")

    enumParameters()

    builder.append(s"] = {\n  value.map(toSome)\n}")

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
