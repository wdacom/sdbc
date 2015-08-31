package com.rocketfuel.sdbc.base.jdbc

import java.sql.{Types, PreparedStatement}

import com.rocketfuel.sdbc.base

trait ParameterSetter extends base.ParameterSetter[PreparedStatement, Int] {
  override def setNone(preparedStatement: PreparedStatement, parameterIndex: Int): Unit = {
    preparedStatement.setNull(parameterIndex, Types.NULL)
  }
}
