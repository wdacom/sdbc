package com.wda.sdbc.cassandra

import com.datastax.driver.core.{TupleType, BoundStatement}
import shapeless.syntax.std.tuple._
import TupleParameterValues.box

trait TupleParameterValues {
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
    value: (T0)
  )(implicit
    dt0: TupleDataType[T0]
  ) extends ParameterValue[(T0)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType).newValue(box(value))
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def Tuple1ToParameterValue[T0](
    value: (T0)
  )(implicit
    dt0: TupleDataType[T0]
  ): Tuple1Parameter[T0] = {
    Tuple1Parameter[T0](value)
  }

  case class Tuple2Parameter[T0, T1](
    value: (T0, T1)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ) extends ParameterValue[(T0, T1)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def Tuple2ToParameterValue[T0, T1](
    value: (T0, T1)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1]
  ): Tuple2Parameter[T0, T1] = {
    Tuple2Parameter[T0, T1](value)
  }

  case class Tuple3Parameter[T0, T1, T2](
    value: (T0, T1, T2)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2]
  ) extends ParameterValue[(T0, T1, T2)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def Tuple3ToParameterValue[T0, T1, T2](
    value: (T0, T1, T2)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2]
  ): Tuple3Parameter[T0, T1, T2] = {
    Tuple3Parameter[T0, T1, T2](value)
  }

  case class Tuple4Parameter[T0, T1, T2, T3](
    value: (T0, T1, T2, T3)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3]
  ) extends ParameterValue[(T0, T1, T2, T3)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
  }

  implicit def Tuple4ToParameterValue[T0, T1, T2, T3](
    value: (T0, T1, T2, T3)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3]
  ): Tuple4Parameter[T0, T1, T2, T3] = {
    Tuple4Parameter[T0, T1, T2, T3](value)
  }

  case class Tuple5Parameter[T0, T1, T2, T3, T4](
    value: (T0, T1, T2, T3, T4)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4]
  ) extends ParameterValue[(T0, T1, T2, T3, T4)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple5Parameter[T0, T1, T2, T3, T4](value)
  }

  case class Tuple6Parameter[T0, T1, T2, T3, T4, T5](
    value: (T0, T1, T2, T3, T4, T5)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5]
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple6Parameter[T0, T1, T2, T3, T4, T5](value)
  }

  case class Tuple7Parameter[T0, T1, T2, T3, T4, T5, T6](
    value: (T0, T1, T2, T3, T4, T5, T6)
  )(implicit
    dt0: TupleDataType[T0],
    dt1: TupleDataType[T1],
    dt2: TupleDataType[T2],
    dt3: TupleDataType[T3],
    dt4: TupleDataType[T4],
    dt5: TupleDataType[T5],
    dt6: TupleDataType[T6]
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple7Parameter[T0, T1, T2, T3, T4, T5, T6](value)
  }

  case class Tuple8Parameter[T0, T1, T2, T3, T4, T5, T6, T7](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple8Parameter[T0, T1, T2, T3, T4, T5, T6, T7](value)
  }

  case class Tuple9Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple9Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8](value)
  }

  case class Tuple10Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple10Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9](value)
  }

  case class Tuple11Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple11Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](value)
  }

  case class Tuple12Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple12Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](value)
  }

  case class Tuple13Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple13Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](value)
  }

  case class Tuple14Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple14Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](value)
  }

  case class Tuple15Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple15Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](value)
  }

  case class Tuple16Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple16Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](value)
  }

  case class Tuple17Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple17Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](value)
  }

  case class Tuple18Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple18Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](value)
  }

  case class Tuple19Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple19Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](value)
  }

  case class Tuple20Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType, dt19.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple20Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](value)
  }

  case class Tuple21Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType, dt19.dataType, dt20.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple21Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](value)
  }

  case class Tuple22Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](
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
  ) extends ParameterValue[(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
    override def set(
      statement: BoundStatement,
      parameterIndex: Int
    ): Unit = {
      val tupleValue = TupleType.of(dt0.dataType, dt1.dataType, dt2.dataType, dt3.dataType, dt4.dataType, dt5.dataType, dt6.dataType, dt7.dataType, dt8.dataType, dt9.dataType, dt10.dataType, dt11.dataType, dt12.dataType, dt13.dataType, dt14.dataType, dt15.dataType, dt16.dataType, dt17.dataType, dt18.dataType, dt19.dataType, dt20.dataType, dt21.dataType).newValue(value.toList.map(box): _*)
      statement.setTupleValue(parameterIndex, tupleValue)
    }
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
    Tuple22Parameter[T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](value)
  }

}

object TupleParameterValues {
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

    def enumImplicitArgs() = {
      for (i <- 0 until arity) {
        builder.append(s"  dt$i: TupleDataType[T$i]")
        if (i != arity - 1) builder.append(",\n")
      }
    }

    builder.append(s"case class Tuple${arity}Parameter[")

    enumParameters()

    builder.append("](\n  value: (")

    enumParameters()

    builder.append(")\n)(implicit\n")

    enumImplicitArgs()

    builder.append("\n) extends ParameterValue[(")

    enumParameters()

    builder.append(")] {\n  override def set(\n    statement: BoundStatement,\n    parameterIndex: Int\n  ): Unit = {\n    val tupleValue = TupleType.of(")

    for (i <- 0 until arity) {
      builder.append(s"dt$i.dataType")
      if (i != arity - 1) builder.append(", ")
    }

    builder.append(").newValue(value.toList.map(box): _*)\n    statement.setTupleValue(parameterIndex, tupleValue)\n  }\n}\n\n")

    builder.append(s"implicit def Tuple${arity}ToParameterValue[")

    enumParameters()

    builder.append("](\n  value: (")

    enumParameters()

    builder.append(s")\n)(implicit\n")

    enumImplicitArgs()

    builder.append(s"\n): Tuple${arity}Parameter[")

    enumParameters()

    builder.append(s"] = {\n  Tuple${arity}Parameter[")

    enumParameters()

    builder.append("](value)\n}")

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
