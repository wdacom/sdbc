package com.wda.sdbc.base

trait Pool {
  self: Connection =>

  type UnderlyingPool

  trait Pool {
    def getConnection(pool: UnderlyingPool): Connection

    def withConnection[T](pool: UnderlyingPool)(f: Connection => T)(implicit isClosable: Closable[Connection]): T = {
      val connection = getConnection(pool)
      try {
        f(connection)
      } finally {
        isClosable.closeQuietly(connection)
      }
    }
  }

}
