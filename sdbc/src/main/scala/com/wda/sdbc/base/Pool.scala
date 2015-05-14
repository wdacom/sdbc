package com.wda.sdbc.base

trait Pool[UnderlyingPool, UnderlyingConnection] {

  def getConnection(pool: UnderlyingPool): UnderlyingConnection

  def withConnection[T](
    pool: UnderlyingPool
  )(f: UnderlyingConnection => T
  )(implicit isClosable: Closable[UnderlyingConnection]
  ): T = {
    val connection = getConnection(pool)
    try {
      f(connection)
    } finally {
      isClosable.closeQuietly(connection)
    }
  }
}
