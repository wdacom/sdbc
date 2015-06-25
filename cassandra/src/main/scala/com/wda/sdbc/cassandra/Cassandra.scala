package com.wda.sdbc.cassandra

import com.wda.sdbc.base.ParameterValueImplicits

abstract class Cassandra
  extends Getters
  with IndexImplicits
  with Row
  with ParameterValues
  with TupleParameterValues
  with ParameterValueImplicits {

}
