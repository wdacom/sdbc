package com.rocketfuel.sdbc.cassandra.datastax.implementation

trait IndexImplicits {

  implicit def IntToIndex(columnIndex: Int): Index = IntIndex(columnIndex)
  
  implicit def StringToIndex(columnName: String): Index = StringIndex(columnName)
  
}
