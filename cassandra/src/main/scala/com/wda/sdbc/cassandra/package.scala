package com.wda.sdbc

import com.datastax.driver.core.{Row => CRow, TupleValue, BoundStatement}

package object cassandra {

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, BoundStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, BoundStatement, Int]

  type Index = PartialFunction[CRow, Int]

  type RowGetter[+T] = base.Getter[CRow, Index, T]

  type TupleGetter[+T] = base.Getter[TupleValue, Int, T]

}
