package com.wda.sdbc.h2

import com.wda.sdbc.jdbc._

trait Updaters
  extends DefaultUpdaters
  with LocalDateUpdater
  with LocalDateTimeUpdater
  with LocalTimeUpdater
  with InstantUpdater
