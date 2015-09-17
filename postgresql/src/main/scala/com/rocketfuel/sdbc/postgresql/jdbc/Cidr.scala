package com.rocketfuel.sdbc.postgresql.jdbc

import java.net.InetAddress

import com.rocketfuel.sdbc.postgresql.jdbc.implementation.PGInetAddress

class Cidr() extends PGInetAddress() {

  setType("cidr")

  var netmask: Option[Short] = None

  override def getValue: String = {
    val result = for {
      inetaddr <- Option(super.getValue)
      actualMask <- netmask
    } yield {
      inetaddr + '/' + actualMask.toString
    }

    result.orNull
  }

  override def setValue(value: String): Unit = {
    val parts = value.split('/')
    super.setValue(parts(0))
    netmask = Some(parts(1).toShort)
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: Cidr =>
        other.inetAddress == this.inetAddress &&
          other.netmask == this.netmask
      case _ =>
        false
    }
  }

  override def hashCode(): Int = {
    super.hashCode() ^ netmask.hashCode()
  }

}

object Cidr {
  def apply(address: InetAddress, netmask: Short): Cidr = {
    val c = new Cidr()
    c.inetAddress = Some(address)
    c.netmask = Some(netmask)
    c
  }
}
