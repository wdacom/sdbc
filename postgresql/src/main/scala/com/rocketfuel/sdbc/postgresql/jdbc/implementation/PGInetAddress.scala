package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress

import org.postgresql.util.PGobject

class PGInetAddress() extends PGobject() {

  var inetAddress: Option[InetAddress] = None

  override def getValue: String = {
    inetAddress.map(_.getHostAddress).orNull
  }

  override def setValue(value: String): Unit = {
    inetAddress = Option(value).map(InetAddress.getByName)
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case pg: PGInetAddress =>
        pg.inetAddress == this.inetAddress
      case _ => false
    }
  }

  override def hashCode(): Int = {
    inetAddress.map(_.hashCode()).getOrElse(0)
  }
}

object PGInetAddress {
  def apply(address: InetAddress): PGInetAddress = {
    val a = new PGInetAddress()
    a.inetAddress = Some(address)
    a
  }
}
