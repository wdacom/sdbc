package com.wda.sdbc.postgresql

import scala.reflect.runtime.universe._

trait InnerTypeName {
  self: PostgreSql =>

  def innerTypeName(tpe: Type): String = {
    val innerType = tpe.dealias.typeArgs.head.dealias
    typeName(innerType)
  }

}
