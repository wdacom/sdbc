package com.wda.sdbc.base

import com.wda.sdbc.base

trait ExecutableMethods[Connection, Execute <: base.Execute[Connection]] {
  trait Executable[Key] {
    def execute(key: Key): Execute
  }

  def execute[Key](key: Key)(implicit ev: Executable[Key], connection: Connection): Unit = {
    ev.execute(key)
  }
}
