package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base.CompiledStatement

trait StringContextMethods {

  implicit class JdbcStringContextMethods(sc: StringContext) {
    private def byNumberName(args: Seq[Any]): Map[String, Option[ParameterValue[_]]] = {
      val argNames = 0.until(sc.parts.count(_.isEmpty)).map(_.toString)
      argNames.zip(args.map(toParameter)).toMap
    }

    private val compiled = CompiledStatement(sc)

    def batch(args: Any*): Batch = {
      sc.checkLengths(args)
      Batch(compiled, Map.empty, Seq(byNumberName(args)))
    }

    def execute(args: Any*): Execute = {
      sc.checkLengths(args)
      Execute(compiled, byNumberName(args))
    }

    def update(args: Any*): Update = {
      sc.checkLengths(args)
      Update(compiled, byNumberName(args))
    }

    def select[T](args: Any*)(implicit converter: Row => T): Select[T] = {
      sc.checkLengths(args)
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
