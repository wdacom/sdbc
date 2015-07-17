package com.wda.sdbc.postgresql

import com.wda.sdbc.jdbc.ResultSetImplicits

abstract class PostgreSql
  extends PostgreSqlCommon
  with SeqParameterValue
  with SeqGetterImplicits
  with ResultSetImplicits {

}
