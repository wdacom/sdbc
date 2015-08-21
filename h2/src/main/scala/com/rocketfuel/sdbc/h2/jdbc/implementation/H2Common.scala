package com.rocketfuel.sdbc.h2.jdbc.implementation

import java.nio.file.Path
import java.sql.DriverManager

import com.rocketfuel.sdbc.base.jdbc.{Index, DBMS}

abstract class H2Common
  extends DBMS
  with Getters
  with Setters
  with Updaters
  with SerializedParameter {
  /**
   * Class name for the DataSource class.
   */
  override def dataSourceClassName: String = "org.h2.jdbcx.JdbcDataSource"

  /**
   * Class name for the JDBC driver class.
   */
  override def driverClassName: String = "org.h2.Driver"

  //http://www.h2database.com/html/cheatSheet.html
  override def jdbcSchemes: Set[String] = {
    Set(
      "h2",
      "h2:mem",
      "h2:tcp"
    )
  }

  /**
   * If the JDBC driver supports the .isValid() method.
   */
  override def supportsIsValid: Boolean = true

  /**
   * The result of getMetaData.getDatabaseProductName
   */
  override def productName: String = "H2"

  /**
   *
   * @param name The name of the database. A name is required if you want multiple connections or dbCloseDelay != Some(0).
   * @param dbCloseDelay The number of seconds to wait after the last connection closes before deleting the database. The default is right away, or Some(0). None means never.
   * @param f
   * @tparam T
   * @return
   */
  def withMemConnection[T](name: String = "", dbCloseDelay: Option[Int] = Some(0))(f: Connection => T): T = {
    val dbCloseDelayArg = s";DB_CLOSE_DELAY=${dbCloseDelay.getOrElse(-1)}"
    val connectionString = s"jdbc:h2:mem:$name$dbCloseDelayArg"
    val connection = DriverManager.getConnection(connectionString)
    try {
      f(connection)
    } finally {
      connection.close()
    }
  }

  def withFileConnection[T](path: Path)(f: Connection => T): T = {
    val connection = DriverManager.getConnection("jdbc:h2:" + path.toFile.getCanonicalPath)

    try {
      f(connection)
    } finally {
      connection.close()
    }
  }

  override implicit val parameterGetter: (Row, Index) => Option[ParameterValue[_]] = {
    (row: Row, columnIndex: Index) =>
      val ix = columnIndex(row)

      val columnType = row.getMetaData.getColumnTypeName(ix)

      columnType match {
        case "INTEGER" =>
          IntGetter(row, ix).map(QInt)
        case "BOOLEAN" =>
          BooleanGetter(row, ix).map(QBoolean)
        case "TINYINT" =>
          ByteGetter(row, ix).map(QByte)
        case "SMALLINT" =>
          ShortGetter(row, ix).map(QShort)
        case "BIGINT" =>
          LongGetter(row, ix).map(QLong)
        case "DECIMAL" =>
          ScalaBigDecimalGetter(row, ix).map(QDecimal)
        case "REAL" =>
          FloatGetter(row, ix).map(QFloat)
        case "TIME" =>
          TimeGetter(row, ix).map(QTime)
        case "DATE" =>
          DateGetter(row, ix).map(QDate)
        case "TIMESTAMP" =>
          TimestampGetter(row, ix).map(QTimestamp)
        case "BLOB" | "VARBINARY" =>
          BytesGetter(row, ix).map(QBytes)
        case "OTHER" =>
          SerializedGetter(row, ix).map(QSerialized)
        case "CHAR" | "VARCHAR" | "VARCHAR_IGNORECASE" =>
          StringGetter(row, ix).map(QString)
        case "UUID" =>
          UUIDGetter(row, ix).map(QUUID)
        case "ARRAY" =>
          throw new NotImplementedError("H2.parameterGetter for ARRAY")
      }
  }
}
