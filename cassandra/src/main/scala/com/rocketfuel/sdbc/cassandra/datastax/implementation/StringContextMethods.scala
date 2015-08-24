package com.rocketfuel.sdbc.cassandra.datastax.implementation

import com.datastax.driver.core.{Row => CRow}
import com.rocketfuel.sdbc.base.CompiledStatement
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

  def toParameter(a: Any): Option[ParameterValue[_]]

}
