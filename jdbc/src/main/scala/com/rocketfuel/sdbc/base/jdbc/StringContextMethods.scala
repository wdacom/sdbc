package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base.CompiledStatement

trait StringContextMethods {

  implicit class JdbcStringContextMethods(sc: StringContext) {
    private def byNumberName(args: Any*): Map[String, Option[ParameterValue[_]]] = {
      val argNames = 0.until(sc.parts.count(_.isEmpty)).map(_.toString)
      argNames.zip(args.map(toParameter)).toMap
    }

    private val compiled = CompiledStatement(sc)

    def batch(args: Any*): Batch = {
      Batch(compiled, Map.empty, Seq(byNumberName(args)))
    }

    def execute(args: Any*): Execute = {
      Execute(compiled, byNumberName(args))
    }

    def update(args: Any*): Update = {
      Update(compiled, byNumberName(args))
    }

    def select[T](args: Any*)(implicit converter: Row => T): Select[T] = {
      Select[T](compiled, byNumberName(args))
    }
  }

  /**
   * This method is for creating parameters out of values used in
   * a StringContext.
   * @param a
   * @return
   */
  protected def toParameter(a: Any): Option[ParameterValue[_]]

}
