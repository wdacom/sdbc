package com.rocketfuel.sdbc.base

import com.rocketfuel.sdbc.base

trait Executable[Key, Connection, Execute <: base.Execute[Connection]] {
  def execute(key: Key): Execute
}

trait ExecutableMethods[Connection, Execute <: base.Execute[Connection]] {

  def execute[Key](key: Key)(implicit ev: base.Executable[Key, Connection, Execute], connection: Connection): Unit = {
    ev.execute(key)
  }

}
