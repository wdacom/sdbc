package com.wda.sdbc

import java.sql.PreparedStatement

package object jdbc {

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, PreparedStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, PreparedStatement, Int]

}
