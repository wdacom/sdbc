package com.rocketfuel.sdbc.h2.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc._

trait Setters
  extends DefaultSetters
  with LocalDateParameter
  with LocalDateTimeParameter
  with LocalTimeParameter
  with InstantParameter {

  val toH2Parameter =
    toParameter orElse
      toLocalDateParameter orElse
      toLocalDateTimeParameter orElse
      toLocalTimeParameter orElse
      toInstantParameter

}
