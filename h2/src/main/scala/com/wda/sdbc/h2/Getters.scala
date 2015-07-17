package com.wda.sdbc.h2

import com.wda.sdbc.jdbc._

trait Getters
  extends DefaultGetters
  with LocalDateGetter
  with LocalDateTimeGetter
  with LocalTimeGetter
  with InstantGetter
