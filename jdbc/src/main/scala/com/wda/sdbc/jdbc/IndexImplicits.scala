package com.wda.sdbc.jdbc

trait IndexImplicits {

  implicit def IntToRowIndex(columnIndex: Int): Index = {
    IntIndex(columnIndex)
  }

  implicit def StringToRowIndex(columnLabel: String): Index = {
    StringIndex(columnLabel)
  }

}