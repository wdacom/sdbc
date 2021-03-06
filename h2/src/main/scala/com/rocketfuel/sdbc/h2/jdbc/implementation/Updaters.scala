package com.rocketfuel.sdbc.h2.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc._

private[sdbc] trait Updaters
  extends DefaultUpdaters
  with LocalDateUpdater
  with LocalDateTimeUpdater
  with LocalTimeUpdater
  with InstantUpdater
