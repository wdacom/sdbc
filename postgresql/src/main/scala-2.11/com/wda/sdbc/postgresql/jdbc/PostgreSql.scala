package com.wda.sdbc.postgresql.jdbc

import com.wda.sdbc.base.jdbc.ResultSetImplicits

abstract class PostgreSql
  extends PostgreSqlCommon
  with SeqParameterValue
  with SeqGetterImplicits
  with ResultSetImplicits {

}
