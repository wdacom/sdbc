package com.wda

trait Closable[UnderlyingClosable] {

  def close(connection: UnderlyingClosable): Unit

  def closeQuietly(closable: UnderlyingClosable): Unit = {
    util.Try(close(closable))
  }

}
