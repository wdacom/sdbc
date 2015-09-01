package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.net.InetAddress
import java.sql.SQLException

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
    inetAddress.hashCode()
  }
}

object PGInetAddress {
  def apply(address: InetAddress): PGInetAddress = {
    val a = new PGInetAddress()
    a.inetAddress = Some(address)
    a
  }
}

trait PGInetAddressImplicits {
  implicit def InetAddressToPGobject(x: InetAddress): PGobject = {
    PGInetAddress(x)
  }

  implicit def PGobjectToInetAddress(x: PGobject): InetAddress = {
    x match {
      case p: PGInetAddress =>
        p.inetAddress.get
      case _ =>
        throw new SQLException("column does not contain an inet")
    }
  }
}
