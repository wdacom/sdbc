package com.wda.sdbc.jdbc.h2

import com.wda.sdbc.base.jdbc._

trait Updaters
  extends DefaultUpdaters
  with LocalDateUpdater
  with LocalDateTimeUpdater
  with LocalTimeUpdater
  with InstantUpdater
