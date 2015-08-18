package com.wda.sdbc.h2.jdbc

import com.wda.sdbc.base.jdbc._

trait Updaters
  extends DefaultUpdaters
  with LocalDateUpdater
  with LocalDateTimeUpdater
  with LocalTimeUpdater
  with InstantUpdater