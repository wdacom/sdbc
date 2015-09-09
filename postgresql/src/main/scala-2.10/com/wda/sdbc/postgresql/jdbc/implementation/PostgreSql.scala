package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.io.{InputStream, Reader}
import java.sql.PreparedStatement
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc.ParameterSetter
import org.json4s._
import org.postgresql.util.PGobject
import scodec.bits.ByteVector

import scala.xml.Node

abstract class PostgreSql
  extends PostgreSqlCommon {

  override implicit val parameterSetter: ParameterSetter = new ParameterSetter {
    /**
     * Pattern match on parameters to get the IsParameter instance for
     * each value, and then call setParameter.
     * @param preparedStatement
     * @param parameterIndex
     * @param parameter
     */
    override def setAny(preparedStatement: PreparedStatement, parameterIndex: Int, parameter: Any): Unit = {
      parameter match {
        case b: Boolean =>
          setParameter[Boolean](preparedStatement, parameterIndex, b)
        case b: ByteVector =>
          setParameter[ByteVector](preparedStatement, parameterIndex, b)
        case b: java.sql.Date =>
          setParameter[java.sql.Date](preparedStatement, parameterIndex, b)
        case b: java.math.BigDecimal =>
          setParameter[java.math.BigDecimal](preparedStatement, parameterIndex, b)
        case b: Double =>
          setParameter[Double](preparedStatement, parameterIndex, b)
        case b: Float =>
          setParameter[Float](preparedStatement, parameterIndex, b)
        case b: Int =>
          setParameter[Int](preparedStatement, parameterIndex, b)
        case b: Long =>
          setParameter[Long](preparedStatement, parameterIndex, b)
        case b: Short =>
          setParameter[Short](preparedStatement, parameterIndex, b)
        case b: String =>
          setParameter[String](preparedStatement, parameterIndex, b)
        case b: java.sql.Time =>
          setParameter[java.sql.Time](preparedStatement, parameterIndex, b)
        case b: java.sql.Timestamp =>
          setParameter[java.sql.Timestamp](preparedStatement, parameterIndex, b)
        case b: Reader =>
          setParameter[Reader](preparedStatement, parameterIndex, b)
        case b: InputStream =>
          setParameter[InputStream](preparedStatement, parameterIndex, b)
        case b: UUID =>
          setParameter[UUID](preparedStatement, parameterIndex, b)
        case b: PGobject =>
          setParameter[PGobject](preparedStatement, parameterIndex, b)
        case b: java.util.Map[_, _] =>
          setParameter[java.util.Map[String, String]](preparedStatement, parameterIndex, b.asInstanceOf[java.util.Map[String, String]])
        case b: Node =>
          setParameter[Node](preparedStatement, parameterIndex, b)
        case b: JValue =>
          setParameter[PGobject](preparedStatement, parameterIndex, b)
      }
    }
  }

}
