package com.wda.sdbc.h2

import com.wda.sdbc.jdbc._

trait Setters
  extends DefaultSetters
  with LocalDateParameter
  with LocalDateTimeParameter
  with LocalTimeParameter
  with InstantParameter
