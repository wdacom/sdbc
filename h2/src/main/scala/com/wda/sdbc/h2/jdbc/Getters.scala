package com.wda.sdbc.h2.jdbc

import com.wda.sdbc.base.jdbc._

trait Getters
  extends DefaultGetters
  with LocalDateGetter
  with LocalDateTimeGetter
  with LocalTimeGetter
  with InstantGetter