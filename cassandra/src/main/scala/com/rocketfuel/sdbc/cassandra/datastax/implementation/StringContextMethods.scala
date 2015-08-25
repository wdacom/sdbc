package com.rocketfuel.sdbc.cassandra.datastax.implementation

import java.net.InetAddress
import java.nio.ByteBuffer

import com.datastax.driver.core.{Row => CRow, _}
import com.rocketfuel.sdbc.base.CompiledStatement
import com.rocketfuel.sdbc.cassandra.datastax.QueryOptions
import com.rocketfuel.sdbc.cassandra.datastax._

trait StringContextMethods {

  implicit class CassandraStringContextMethods(sc: StringContext) {
    private def byNumberName(args: Any*): Map[String, Option[ParameterValue[_]]] = {
      val argNames = 0.until(sc.parts.count(_.isEmpty)).map(_.toString)
      argNames.zip(args.map(toParameter)).toMap
    }

    private val compiled = CompiledStatement(sc)

    def execute(args: Any*): Execute = {
      Execute(compiled, byNumberName(args), QueryOptions.default)
    }

    def select[T](args: Any*)(implicit converter: CRow => T): Select[T] = {
      Select[T](compiled, byNumberName(args), QueryOptions.default)
    }
  }

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

  /**
   * Currently Scala tuples are not supported.
   * @param a
   * @return
   */
  private def toParameter(a: Any): Option[implementation.ParameterValue[_]] = {
    a match {
      case null | None | Some(null) =>
        None
      case Some(a) =>
        Some(toParameter(a)).flatten
      case _ =>
        Some(toParameterRaw(a))
    }
  }

}
