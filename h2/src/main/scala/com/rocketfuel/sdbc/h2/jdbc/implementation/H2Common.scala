package com.rocketfuel.sdbc.h2.jdbc.implementation

import java.nio.file.Path
import java.sql.DriverManager

import com.rocketfuel.sdbc.base.jdbc
import com.rocketfuel.sdbc.base.jdbc.DBMS

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

  override implicit val ParameterGetter: Getter[ParameterValue[_]] = {
    (row: Row, ix: Index) =>
      val columnType = row.getMetaData.getColumnTypeName(ix(row))

      columnType match {
        case "INTEGER" =>
          IntGetter(row, ix).map(QInt.apply)
        case "BOOLEAN" =>
          BooleanGetter(row, ix).map(QBoolean.apply)
        case "TINYINT" =>
          ByteGetter(row, ix).map(QByte.apply)
        case "SMALLINT" =>
          ShortGetter(row, ix).map(QShort.apply)
        case "BIGINT" =>
          LongGetter(row, ix).map(QLong.apply)
        case "DECIMAL" =>
          JavaBigDecimalGetter(row, ix).map(QBigDecimal.apply)
        case "REAL" =>
          FloatGetter(row, ix).map(QFloat.apply)
        case "TIME" =>
          TimeGetter(row, ix).map(QTime.apply)
        case "DATE" =>
          DateGetter(row, ix).map(QDate.apply)
        case "TIMESTAMP" =>
          TimestampGetter(row, ix).map(QTimestamp.apply)
        case "BLOB" | "VARBINARY" =>
          ByteVectorGetter(row, ix).map(bv => QBytes(bv.toArray))
        case "OTHER" =>
          SerializedGetter(row, ix).map(QSerialized)
        case "CHAR" | "VARCHAR" | "VARCHAR_IGNORECASE" =>
          StringGetter(row, ix).map(QString.apply)
        case "UUID" =>
          UUIDGetter(row, ix).map(QUUID.apply)
        case "ARRAY" =>
          throw new NotImplementedError("H2.ParameterGetter for ARRAY")
      }
  }

  override protected def toParameter(a: Any): Option[jdbc.ParameterValue[_]] = {
    a match {
      case null | None | Some(null) =>
        None
      case Some(a) =>
        Some(toParameter(a)).flatten
      case a =>
        Some(toH2Parameter(a))
    }
  }
}
