package com.wda.sdbc.jdbc

import _root_.scalaz.stream.Process

package object scalaz {

  implicit def ProcessToJdbcProcess(x: Process.type): JdbcProcess.type = {
    JdbcProcess
  }

}
