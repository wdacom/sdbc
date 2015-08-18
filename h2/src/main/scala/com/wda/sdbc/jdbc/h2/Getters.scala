package com.wda.sdbc.jdbc.h2

import com.wda.sdbc.base.jdbc._

trait Getters
  extends DefaultGetters
  with LocalDateGetter
  with LocalDateTimeGetter
  with LocalTimeGetter
  with InstantGetter
