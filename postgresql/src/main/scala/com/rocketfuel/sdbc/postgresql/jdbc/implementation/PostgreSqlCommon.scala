package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import com.rocketfuel.sdbc.base
import com.rocketfuel.sdbc.base.jdbc._
import com.rocketfuel.sdbc.postgresql.jdbc
import org.postgresql.PGConnection

abstract class PostgreSqlCommon
  extends DBMS
  with Setters
  with HasOffsetTimeFormatter
  with HasOffsetDateTimeFormatter
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
    connection.unwrap[PGConnection](classOf[PGConnection]).addDataType("ltree", classOf[jdbc.LTree])
  }

  override implicit val ParameterGetter: Getter[base.jdbc.ParameterValue[_]] = {
    (row: Row, columnIndex: Index) =>
      val ix = columnIndex(row)

      val columnType = row.getMetaData.getColumnTypeName(ix)

      columnType match {
        case "int4" | "serial" =>
          IntGetter(row, ix).map(QInt)
        case "bool" =>
          BooleanGetter(row, ix).map(QBoolean)
        case "int2" =>
          ShortGetter(row, ix).map(QShort)
        case "int8" | "bigserial" =>
          LongGetter(row, ix).map(QLong)
        case "numeric" =>
          ScalaBigDecimalGetter(row, ix).map(QDecimal)
        case "float4" =>
          FloatGetter(row, ix).map(QFloat)
        case "float8" =>
          DoubleGetter(row, ix).map(QDouble)
        case "time" =>
          TimeGetter(row, ix).map(QTime)
        case "timetz" =>
          OffsetTimeGetter(row, ix).map(QOffsetTime)
        case "date" =>
          DateGetter(row, ix).map(QDate)
        case "timestamp" =>
          TimestampGetter(row, ix).map(QTimestamp)
        case "timestamptz" =>
          OffsetDateTimeGetter(row, ix).map(QOffsetDateTime)
        case "bytea" =>
          BytesGetter(row, ix).map(QBytes)
        case "varchar" | "bpchar" | "text" =>
          StringGetter(row, ix).map(QString)
        case "uuid" =>
          UUIDGetter(row, ix).map(QUUID)
        case "xml" =>
          XMLGetter(row, ix).map(QXML)
        case "json" | "jsonb" =>
          JValueGetter(row, ix).map(j => QJSON(j)(org.json4s.DefaultFormats))
        case array if array.startsWith("_") =>
          throw new NotImplementedError("PostgreSql.parameterGetter for arrays")
      }
  }

}
