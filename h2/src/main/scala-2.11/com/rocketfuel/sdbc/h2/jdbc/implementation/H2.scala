package com.rocketfuel.sdbc.h2.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc

class H2
  extends H2Common
  with SeqParameter {

  type QSeq[T] = jdbc.QSeq[T]
  val QSeq = jdbc.QSeq

}
