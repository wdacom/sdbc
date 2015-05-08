package com.wda.sdbc.base

trait Closable[T] {
  def close(connection: T): Unit

  def closeQuietly(connection: T): Unit = {
    util.Try(close(connection))
  }
}
