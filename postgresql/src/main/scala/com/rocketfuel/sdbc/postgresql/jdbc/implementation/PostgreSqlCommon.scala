package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.io.{InputStream, Reader}
import java.net.InetAddress
import java.sql.{Types, PreparedStatement}
import java.time.Instant
import java.util.UUID
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc
import org.postgresql.PGConnection
import org.postgresql.util.PGobject

abstract class PostgreSqlCommon
  extends DBMS
  with Setters
  with IntervalImplicits
  with ConnectionImplicits
  with Getters
  with Java8DefaultUpdaters {

  override def dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
  override def driverClassName = "org.postgresql.Driver"
  override def jdbcSchemes = Set("postgresql")
  override def productName: String = "PostgreSQL"
  override val supportsIsValid: Boolean = true

  /**
   * Perform any connection initialization that should be done when a connection
   * is created. EG add a type mapping.
   *
   * By default this method does nothing.
   * @param connection
   */
  override def initializeConnection(connection: java.sql.Connection): Unit = {
    val pgConnection = connection.unwrap[PGConnection](classOf[PGConnection])
    pgConnection.addDataType("ltree", classOf[jdbc.LTree])
    pgConnection.addDataType("inet", classOf[PGInetAddress])
    pgConnection.addDataType("timestamptz", classOf[PGTimestampTz])
    pgConnection.addDataType("timetz", classOf[PGTimeTz])
    pgConnection.addDataType("json", classOf[PGJson])
    pgConnection.addDataType("jsonb", classOf[PGJson])
  }

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
        case b: Array[Byte] =>
          setParameter[Array[Byte]](preparedStatement, parameterIndex, b)
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
      }
    }
  }

  override implicit val ParameterGetter: Getter[ParameterValue] = {
    (row: Row, columnIndex: Index) =>
      val ix = columnIndex(row)

      val columnType = row.columnTypes(columnIndex(row))

      columnType match {
        case "int4" | "serial" =>
          IntGetter(row, ix).map(ParameterValue)
        case "bool" =>
          BooleanGetter(row, ix)
        case "int2" =>
          ShortGetter(row, ix)
        case "int8" | "bigserial" =>
          LongGetter(row, ix)
        case "numeric" =>
          JavaBigDecimalGetter(row, ix)
        case "float4" =>
          FloatGetter(row, ix)
        case "float8" =>
          DoubleGetter(row, ix)
        case "time" =>
          TimeGetter(row, ix)
        case "timetz" =>
          OffsetTimeGetter(row, ix)
        case "date" =>
          DateGetter(row, ix)
        case "timestamp" =>
          TimestampGetter(row, ix)
        case "timestamptz" =>
          OffsetDateTimeGetter(row, ix)
        case "bytea" =>
          ByteVectorGetter(row, ix)
        case "varchar" | "bpchar" | "text" =>
          StringGetter(row, ix)
        case "uuid" =>
          UUIDGetter(row, ix)
        case "xml" =>
          XMLGetter(row, ix)
        case "json" | "jsonb" =>
          JValueGetter(row, ix)
        case array if array.startsWith("_") =>
          throw new NotImplementedError("PostgreSql.parameterGetter for arrays")
      }
  }

  override def toParameter(a: Any): Option[ParameterValue] = {
    a match {
      case null | None | Some(null) =>
        None
      case Some(a) =>
        Some(toParameter(a)).flatten
      case _ =>
        Some(toPostgresqlParameter(a))
    }
  }
}
