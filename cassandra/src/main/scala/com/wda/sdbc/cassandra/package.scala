package com.wda.sdbc

import com.datastax.driver.core.{PreparedStatement, Row => CRow}

package object cassandra {

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, PreparedStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, PreparedStatement, Int]

  type Index = PartialFunction[CRow, Int]

  type Getter[+T] = base.Getter[CRow, Index, T]

}
