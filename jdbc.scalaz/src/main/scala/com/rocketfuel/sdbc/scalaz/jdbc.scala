package com.rocketfuel.sdbc.scalaz

import scalaz.stream.Process

object jdbc {

  implicit def ProcessToJdbcProcess(x: Process.type): JdbcProcess.type = {
    JdbcProcess
  }

}
