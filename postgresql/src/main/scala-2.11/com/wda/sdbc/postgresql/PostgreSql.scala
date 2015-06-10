package com.wda.sdbc.postgresql

import com.wda.sdbc.jdbc.{ResultSetImplicits, IndexImplicits}

abstract class PostgreSql
  extends PostgreSqlCommon
  with SeqParameterValue
  with IndexImplicits
  with ResultSetImplicits
  with SeqGetter {

}
