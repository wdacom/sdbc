package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import com.rocketfuel.sdbc.base.jdbc.ResultSetImplicits

abstract class PostgreSql
  extends PostgreSqlCommon
  with SeqParameterValue
  with SeqGetterImplicits
  with ResultSetImplicits {

}
