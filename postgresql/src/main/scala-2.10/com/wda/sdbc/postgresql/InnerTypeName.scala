package com.wda.sdbc.postgresql

import scala.reflect.runtime.universe._

trait InnerTypeName {
  self: PostgreSql =>

  def innerTypeName(tpe: Type): String = {
    val innerType = tpe.asInstanceOf[TypeRefApi].args.head
    typeName(innerType)
  }

}
