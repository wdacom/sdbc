package com.wda.sdbc.jdbc.h2

import com.wda.sdbc.base.jdbc._

trait Setters
  extends DefaultSetters
  with LocalDateParameter
  with LocalDateTimeParameter
  with LocalTimeParameter
  with InstantParameter
