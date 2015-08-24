package com.rocketfuel.sdbc.cassandra.datastax.implementation

import java.net.InetAddress
import java.nio.ByteBuffer

import com.datastax.driver.core._
import com.rocketfuel.sdbc.base.ParameterValueImplicits
import com.rocketfuel.sdbc.cassandra.datastax.implementation

abstract class Cassandra
  extends RowGetters
  with RowGetterImplicits
  with IndexImplicits
  with RowMethods
  with ParameterValues
  with TupleParameterValues
  with TupleDataTypes
  with ParameterValueImplicits
  with TupleGetters
  with SessionMethods
  with ExecutableMethods
  with SelectableMethods
  with StringContextMethods {

  type ParameterValue[+T] = implementation.ParameterValue[T]

  type ParameterList = implementation.ParameterList

  type Session = implementation.Session

  type Cluster = implementation.Cluster

  type Executable[Key] = implementation.Executable[Key]

  type Selectable[Key, Value] = implementation.Selectable[Key, Value]

  private def toParameterRaw(a: Any): implementation.ParameterValue[_] = {
    a match {
      case b: Boolean => b
      case b: java.lang.Boolean => b
      case b: ByteBuffer => b
      case a: Array[Byte] => a
      case d: java.sql.Date => d
      case d: java.math.BigDecimal => d
      case d: BigDecimal => d
      case d: Double => d
      case d: java.lang.Double => d
      case f: Float => f
      case f: java.lang.Float => f
      case i: InetAddress => i
      case i: Int => i
      case i: java.lang.Integer => i
      case s: Seq[_] => s
      case l: java.util.List[_] => l
      case l: Long => l
      case l: java.lang.Long => l
      case m: java.util.Map[_, _] => m
      case m: Map[_, _] => m
      case s: java.util.Set[_] => s
      case s: Set[_] => s
      case s: String => s
      case u: java.util.UUID => u
      case t: Token => t
      case t: TupleValue => t
      case u: UDTValue => u
      case b: java.math.BigInteger => b
    }
  }
  
  override def toParameter(a: Any): Option[implementation.ParameterValue[_]] = {
    a match {
      case null | None =>
        None
      case Some(a) =>
        Some(toParameter(a)).flatten
      case _ =>
        Some(toParameterRaw(a))
    }
  }

}
