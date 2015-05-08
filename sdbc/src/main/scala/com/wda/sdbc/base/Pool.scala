package com.wda.sdbc.base

trait Pool {
  self: Connection =>

  type UnderlyingPool

  trait Pool {
    def getConnection(pool: UnderlyingPool): UnderlyingConnection

    def withConnection[T](pool: UnderlyingPool)(f: UnderlyingConnection => T): T = {
      val connection = getConnection(pool)
      try {
        f(connection)
      } finally {
        isClosableConnection.closeQuietly(connection)
      }
    }
  }

}
