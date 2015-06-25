package com.wda.sdbc

import com.datastax.driver.core.{Row => CRow, BoundStatement}

package object cassandra {

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, BoundStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, BoundStatement, Int]

  type Index = PartialFunction[CRow, Int]

  type Getter[+T] = base.Getter[CRow, Index, T]

}
