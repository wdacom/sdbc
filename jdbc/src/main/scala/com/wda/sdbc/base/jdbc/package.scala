package com.wda.sdbc.base

import java.sql.{Types, PreparedStatement}
import com.wda.sdbc.base
import com.wda.CaseInsensitiveOrdering
import com.zaxxer.hikari.HikariConfig

package object jdbc
  extends HikariImplicits
  with ResultSetImplicits
  with base.BatchableMethods[java.sql.Connection, Batch]
  with base.UpdatableMethods[java.sql.Connection, Update]
  with base.SelectableMethods[java.sql.Connection, Select]
  with base.ExecutableMethods[java.sql.Connection, Execute] {

  private val dataSources: collection.mutable.Map[String, DBMS] = collection.mutable.Map.empty

  private val jdbcSchemes: collection.mutable.Map[String, DBMS] = {
    import scala.collection.convert.decorateAsScala._
    //Scala's collections don't contain an ordered mutable map,
    //so just use java's.
    new java.util.TreeMap[String, DBMS](CaseInsensitiveOrdering).asScala
  }

  private val productNames: collection.mutable.Map[String, DBMS] = collection.mutable.Map.empty

  private val jdbcURIRegex = "(?i)jdbc:(.+):.*".r

  private [jdbc] def register(dbms: DBMS): Unit = {
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

  type ParameterizedQuery[Self <: ParameterizedQuery[Self]] = base.ParameterizedQuery[Self, PreparedStatement, Int]

  type ParameterValue[+T] = base.ParameterValue[T, PreparedStatement, Int]

  type ParameterList = Seq[(String, Option[ParameterValue[_]])]

  type Index = PartialFunction[Row, Int]

  type Getter[+T] = base.Getter[Row, Index, T]

  type Connection = java.sql.Connection

  type Batchable[Key] = base.Batchable[Key, Connection, Batch]

  type Executable[Key] = base.Executable[Key, Connection, Execute]

  type Selectable[Key, Value] = base.Selectable[Key, Value, Connection, Select[Value]]

  type Updatable[Key] = base.Updatable[Key, Connection, Update]

  private [jdbc] def prepare(
    queryText: String,
    parameterValues: Map[String, Option[ParameterValue[_]]],
    parameterPositions: Map[String, Set[Int]]
  )(implicit connection: Connection
  ): PreparedStatement = {
    val preparedStatement = connection.prepareStatement(queryText)

    bind(preparedStatement, parameterValues, parameterPositions)

    preparedStatement
  }

  private [jdbc] def bind(
    preparedStatement: PreparedStatement,
    parameterValues: Map[String, Option[ParameterValue[_]]],
    parameterPositions: Map[String, Set[Int]]
  ): Unit = {
    for ((key, maybeValue) <- parameterValues) {
      val parameterIndices = parameterPositions(key)

      maybeValue match {
        case None =>
          for (parameterIndex <- parameterIndices) {
            preparedStatement.setNull(parameterIndex, Types.NULL)
          }
        case Some(value) =>
          for (parameterIndex <- parameterIndices) {
            value.set(preparedStatement, parameterIndex)
          }
      }
    }
  }

}