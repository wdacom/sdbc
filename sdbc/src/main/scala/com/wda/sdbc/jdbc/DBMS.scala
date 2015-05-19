package com.wda.sdbc.jdbc

import java.sql._

import com.wda.CaseInsensitiveOrdering
import com.wda.sdbc.base
import com.zaxxer.hikari.HikariConfig

abstract class DBMS
  extends Pool
  with JdbcQueryMethods
  with base.Select[Connection, PreparedStatement, ResultSet, ResultSet]
  with base.SelectForUpdate[Connection, PreparedStatement, ResultSet, ResultSet]
  with JdbcGetter
  with base.GetterImplicits[ResultSet] {
  self =>

  type UnderlyingConnection = Connection

  type UnderlyingResultSet = ResultSet

  type UnderlyingRow = ResultSet

  type UnderlyingPreparedStatement = PreparedStatement

  type Query = base.Query[Query, UnderlyingConnection, UnderlyingPreparedStatement, UnderlyingResultSet, UnderlyingRow]

  type Update = base.Update[Connection, PreparedStatement, ResultSet, ResultSet]

  val Update = base.Update

  type Batch = base.Batch[Connection, PreparedStatement, ResultSet, ResultSet]

  val Batch = base.Batch

  type Row = base.Row[UnderlyingRow]

  implicit val isRow: Row = new Row {
    override def findColumnIndex(row: UnderlyingRow, columnName: String): Option[Int] = {
      val meta = row.getMetaData
      for (columnIndex <- 0.until(meta.getColumnCount)) {
        if (meta.getColumnName(columnIndex) == columnName) {
          return Some(columnIndex)
        }
      }
      None
    }
  }

  /**
   * Class name for the DataSource class.
   */
  def dataSourceClassName: String

  /**
   * Class name for the JDBC driver class.
   */
  def driverClassName: String

  def jdbcSchemes: Set[String]

  /**
   * The result of getMetaData.getDatabaseProductName
   */
  def productName: String

  /**
   * If the JDBC driver supports the .isValid() method.
   */
  def supportsIsValid: Boolean

  /**
   * Perform any connection initialization that should be done when a connection
   * is created. EG add a type mapping.
   *
   * By default this method does nothing.
   * @param connection
   */
  def initializeConnection(connection: java.sql.Connection): Unit = {

  }

  DBMS.register(this)

}

object DBMS {

  private val dataSources: collection.mutable.Map[String, DBMS] = collection.mutable.Map.empty

  private val jdbcSchemes: collection.mutable.Map[String, DBMS] = {
    import scala.collection.convert.decorateAsScala._
    //Scala's collections don't contain an ordered mutable map,
    //so just use java's.
    new java.util.TreeMap[String, DBMS](CaseInsensitiveOrdering).asScala
  }

  private val productNames: collection.mutable.Map[String, DBMS] = collection.mutable.Map.empty

  private val jdbcURIRegex = "(?i)jdbc:(.+):.*".r

  private def register(dbms: DBMS): Unit = {
    this.synchronized {
      dataSources(dbms.dataSourceClassName) = dbms
      for (scheme <- dbms.jdbcSchemes) {
        jdbcSchemes(scheme) = dbms
      }
      productNames(dbms.productName) = dbms
      Class.forName(dbms.driverClassName)
    }
  }

  def ofJdbcUrl(connectionString: String): DBMS = {
    val jdbcURIRegex(scheme) = connectionString

    jdbcSchemes(scheme)
  }

  def ofDataSourceClassName(toLookup: String): DBMS = {
    dataSources(toLookup)
  }

  def of(config: HikariConfig): DBMS = {
    val dataSourceClassDbms = Option(config.getDataSourceClassName).flatMap(dataSources.get)
    val urlDbms = Option(config.getJdbcUrl).map(ofJdbcUrl)
    dataSourceClassDbms.
    orElse(urlDbms).
    get
  }

  def of(c: java.sql.Connection): DBMS = {
    productNames(c.getMetaData.getDatabaseProductName)
  }

  def of(s: java.sql.PreparedStatement): DBMS = {
    of(s.getConnection)
  }

  def of(s: java.sql.Statement): DBMS = {
    of(s.getConnection)
  }

  def of(r: java.sql.ResultSet): DBMS = {
    of(r.getStatement)
  }

}
