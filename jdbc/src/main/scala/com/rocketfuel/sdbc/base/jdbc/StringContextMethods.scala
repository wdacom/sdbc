package com.rocketfuel.sdbc.base.jdbc

import com.rocketfuel.sdbc.base.CompiledStatement

trait StringContextMethods {

  implicit class JdbcStringContextMethods(sc: StringContext) {
    private def byNumberName(args: Seq[Any]): Map[String, Option[ParameterValue[_]]] = {
      val argNames = 0.until(sc.parts.size - 1).map(_.toString)
      val compiled = argNames.zip(args.map(toParameter)).toMap
      compiled
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

    def select(args: Any*)(implicit getter: Getter[ParameterValue[_]]): Select[ImmutableRow] = {
      sc.checkLengths(args)
      Select[ImmutableRow](compiled, byNumberName(args))
    }

    def selectForUpdate(args: Any*): SelectForUpdate = {
      sc.checkLengths(args)
      val argsByName = byNumberName(args)
      SelectForUpdate(compiled, argsByName)
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
