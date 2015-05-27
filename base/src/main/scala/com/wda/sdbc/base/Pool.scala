package com.wda.sdbc.base

import com.wda.Closable

trait Pool[UnderlyingPool, UnderlyingConnection] {

  def getConnection(): UnderlyingConnection

  def withConnection[T](f: UnderlyingConnection => T
  )(implicit isClosable: Closable[UnderlyingConnection]
  ): T = {
    val connection = getConnection()
    try {
      f(connection)
    } finally {
      isClosable.closeQuietly(connection)
    }
  }
}
