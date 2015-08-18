package com.wda.sdbc.h2.jdbc.implementation

import com.wda.sdbc.base.jdbc._

trait Setters
  extends DefaultSetters
  with LocalDateParameter
  with LocalDateTimeParameter
  with LocalTimeParameter
  with InstantParameter
