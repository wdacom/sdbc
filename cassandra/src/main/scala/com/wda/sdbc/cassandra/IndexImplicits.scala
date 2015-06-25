package com.wda.sdbc.cassandra

trait IndexImplicits {

  implicit def IntToIndex(columnIndex: Int): Index = IntIndex(columnIndex)
  
  implicit def StringToIndex(columnName: String): Index = StringIndex(columnName)
  
}
