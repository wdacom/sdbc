package com.rocketfuel.sdbc.h2.jdbc.implementation

import java.io.{InputStream, Reader}
import java.sql.PreparedStatement
import java.util.UUID

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.h2.jdbc.Serialized
import scodec.bits.ByteVector

class H2
  extends H2Common
  with SeqParameter {

  type QSeq[T] = jdbc.QSeq[T]
  val QSeq = jdbc.QSeq

  override implicit val parameterSetter: ParameterSetter = new ParameterSetter {
    /**
     * Pattern match to get the IsParameter instance for
     * a value, and then call setParameter.
     *
     * This method is to be implemented on a per-DBMS basis.
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
        case b: Byte =>
          setParameter[Byte](preparedStatement, parameterIndex, b)
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
        case b: Serialized =>
          setParameter[Serialized](preparedStatement, parameterIndex, b)
        case b: QSeq[_] =>
          setParameter[QSeq[_]](preparedStatement, parameterIndex, b)
      }
    }
  }


}
