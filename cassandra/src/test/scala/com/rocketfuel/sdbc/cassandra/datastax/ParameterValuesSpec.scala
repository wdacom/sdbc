package com.rocketfuel.sdbc.cassandra.datastax

import java.net.InetAddress
import java.nio.ByteBuffer
import java.util
import java.util.{Date, UUID}
import org.scalatest.FunSuite
import scodec.bits.ByteVector
import java.math.{BigDecimal => JBigDecimal}

class ParameterValuesSpec extends FunSuite {


  test("implicit Boolean conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = false")
  }

  test("implicit Boxed Boolean conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = java.lang.Boolean.valueOf(false)")
  }

  test("implicit ByteVector conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = ByteVector.empty")
  }

  test("implicit ByteBuffer conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = ByteBuffer.wrap(Array.emptyByteArray)")
  }

  test("implicit Array[Byte] conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Array.emptyByteArray")
  }

  test("implicit Date conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = new Date(0)")
  }

  test("implicit Java BigDecimal conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = JBigDecimal.valueOf(0L)")
  }

  test("implicit Scala BigDecimal conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = BigDecimal(0L)")
  }

  test("implicit Double conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = 3.0")
  }

  test("implicit Boxed Double conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = java.lang.Double.valueOf(0.0)")
  }

  test("implicit Float conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = 3.0F")
  }

  test("implicit Boxed Float conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = java.lang.Float.valueOf(0.0F)")
  }

  test("implicit InetAddress conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = InetAddress.getByAddress(Array[Byte](127,0,0,1))")
  }

  test("implicit Int conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = 3")
  }

  test("implicit Boxed Int conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = java.lang.Integer.valueOf(0)")
  }

  test("implicit Seq conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Seq.empty[Int]")
  }

  test("implicit java List conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = new util.LinkedList[Int]()")
  }

  test("implicit Long conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = 3L")
  }

  test("implicit Boxed Long conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = java.lang.Long.valueOf(0L)")
  }

  test("implicit java Map conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = new util.HashMap[String, String]()")
  }

  test("implicit Map conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Map.empty[String, String]")
  }

  test("implicit Option[String] conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Some(\"hello\")")
  }

  test("implicit scala.BigDecimal conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = BigDecimal(1)")
  }

  test("implicit java Set conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = new util.HashSet[String]()")
  }

  test("implicit Set conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = Set.empty[String]")
  }

  test("implicit String conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = \"\"")
  }

  test("implicit UUID conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = UUID.randomUUID()")
  }

  test("implicit Tuple2 conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = (1, 1)")
  }

  test("implicit Tuple3 conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = (1, 1, 1)")
  }

  test("implicit BigInteger conversion works") {
    assertCompiles("val _: Option[ParameterValue[_]] = java.math.BigInteger.valueOf(0L)")
  }

}
