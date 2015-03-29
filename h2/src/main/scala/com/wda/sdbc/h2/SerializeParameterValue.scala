package com.wda.sdbc.h2

trait SerializeParameterValue {
  case class Serialize(
    value: AnyRef
  )
}
