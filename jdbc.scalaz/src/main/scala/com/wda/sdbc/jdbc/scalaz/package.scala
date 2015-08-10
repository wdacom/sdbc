package com.wda.sdbc.jdbc

import _root_.scalaz.stream.Process

package object scalaz {

  implicit def ProcessToProcessMethods(x: Process.type): ProcessMethods.type = {
    ProcessMethods
  }

}
