package com.rocketfuel.sdbc.h2.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc._

trait Setters
  extends DefaultSetters
  with LocalDateParameter
  with LocalDateTimeParameter
  with LocalTimeParameter
  with InstantParameter
