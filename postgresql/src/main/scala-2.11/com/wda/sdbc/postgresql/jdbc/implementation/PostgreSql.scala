package com.wda.sdbc.postgresql.jdbc.implementation

import com.wda.sdbc.base.jdbc.ResultSetImplicits

abstract class PostgreSql
  extends PostgreSqlCommon
  with SeqParameterValue
  with SeqGetterImplicits
  with ResultSetImplicits {

}
