package com.wda.sdbc.base

import com.wda.sdbc.base

trait Executable[Connection, Key, E <: Execute[Connection]] {
  def execute(key: Key): E
}

trait ExecutableMethods[Connection] {
  type Execute <: base.Execute[Connection]

  def execute[Key](key: Key)(implicit ev: Executable[Connection, Key, Execute], connection: Connection): Unit = {
    ev.execute(key)
  }
}
