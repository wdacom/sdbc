package com.rocketfuel.sdbc.base.jdbc

import java.io.{Closeable, Reader, InputStream}
import java.math.BigDecimal
import java.net.URL
import java.sql.{Array => JdbcArray, _}
import java.util
import java.util.Calendar

/**
 * A class which wraps the read-only parts of a JDBC `ResultSet`.
 *
 * For an updatable version, see `SelectForUpdate`.
 *
 * There are two good ways to get information out of a Row. If you want to use
 * generics, use [[get()]]. If you want to use pattern matching, use [[get[ParameterValue[_]()]].
 * You can also use the various get methods that JDBC provides.
 *
 * @param underlying
 */
class Row private[jdbc](protected[jdbc] val underlying: ResultSet)
  extends Closeable
  with Wrapper {

  override def unwrap[T](iface: Class[T]): T = {
    if (iface.isInstance(this)) {
      this.asInstanceOf[T]
    } else if (iface.isInstance(underlying)) {
      underlying.asInstanceOf[T]
    } else {
      underlying.unwrap[T](iface)
    }
  }

  override def isWrapperFor(iface: Class[_]) = {
    iface.isInstance(this) ||
      iface.isInstance(underlying) ||
      underlying.isWrapperFor(iface: Class[_])
  }

  lazy val columnTypes: IndexedSeq[String] = {
    val metadata = underlying.getMetaData

    IndexedSeq.tabulate(metadata.getColumnCount)(ix => metadata.getColumnTypeName(ix + 1))
  }

  def get[T](columnIndex: Index)(implicit getter: Getter[T]): Option[T] = {
    getter(this, columnIndex)
  }

  def getParametersByName(implicit getter: Getter[ParameterValue[_]]): Map[String, Option[ParameterValue[_]]] = {
    val metadata = underlying.getMetaData
    getParameters.zipWithIndex.foldLeft(Map.empty[String, Option[ParameterValue[_]]]) {
      case (accum, (parameter, ix)) =>
        val columnName = metadata.getColumnName(ix + 1)
        accum + (columnName -> parameter)
    }
  }

  def getParameters(implicit getter: Getter[ParameterValue[_]]): IndexedSeq[Option[ParameterValue[_]]] = {
    IndexedSeq.tabulate(underlying.getMetaData.getColumnCount) { ix =>
      get[ParameterValue[_]](IntIndex(ix))
    }
  }

  def getType: Int = underlying.getType()

  def isBeforeFirst: Boolean = underlying.isBeforeFirst()

  def getTimestamp(columnIndex: Int): Timestamp = underlying.getTimestamp(columnIndex: Int)

  def getTimestamp(columnLabel: String): Timestamp = underlying.getTimestamp(columnLabel: String)

  def getTimestamp(columnIndex: Int, cal: Calendar): Timestamp = underlying.getTimestamp(columnIndex: Int, cal: Calendar)

  def getTimestamp(columnLabel: String, cal: Calendar): Timestamp = underlying.getTimestamp(columnLabel: String, cal: Calendar)

  def clearWarnings(): Unit = underlying.clearWarnings()

  def isAfterLast: Boolean = underlying.isAfterLast()

  def getBinaryStream(columnIndex: Int): InputStream = underlying.getBinaryStream(columnIndex: Int)

  def getBinaryStream(columnLabel: String): InputStream = underlying.getBinaryStream(columnLabel: String)

  def isLast: Boolean = underlying.isLast()

  def getNClob(columnIndex: Int): NClob = underlying.getNClob(columnIndex: Int)

  def getNClob(columnLabel: String): NClob = underlying.getNClob(columnLabel: String)

  def getCharacterStream(columnIndex: Int): Reader = underlying.getCharacterStream(columnIndex: Int)

  def getCharacterStream(columnLabel: String): Reader = underlying.getCharacterStream(columnLabel: String)

  def getDouble(columnIndex: Int): Double = underlying.getDouble(columnIndex: Int)

  def getDouble(columnLabel: String): Double = underlying.getDouble(columnLabel: String)

  def getArray(columnIndex: Int): JdbcArray = underlying.getArray(columnIndex: Int)

  def getArray(columnLabel: String): JdbcArray = underlying.getArray(columnLabel: String)

  def isFirst: Boolean = underlying.isFirst()

  def getURL(columnIndex: Int): URL = underlying.getURL(columnIndex: Int)

  def getURL(columnLabel: String): URL = underlying.getURL(columnLabel: String)

  def getMetaData: ResultSetMetaData = underlying.getMetaData()

  def getRowId(columnIndex: Int): RowId = underlying.getRowId(columnIndex: Int)

  def getRowId(columnLabel: String): RowId = underlying.getRowId(columnLabel: String)

  def getBigDecimal(columnIndex: Int): BigDecimal = underlying.getBigDecimal(columnIndex: Int)

  def getBigDecimal(columnLabel: String): BigDecimal = underlying.getBigDecimal(columnLabel: String)

  def getFloat(columnIndex: Int): Float = underlying.getFloat(columnIndex: Int)

  def getFloat(columnLabel: String): Float = underlying.getFloat(columnLabel: String)

  def getClob(columnIndex: Int): Clob = underlying.getClob(columnIndex: Int)

  def getClob(columnLabel: String): Clob = underlying.getClob(columnLabel: String)

  def getRow: Int = underlying.getRow()

  def getLong(columnIndex: Int): Long = underlying.getLong(columnIndex: Int)

  def getLong(columnLabel: String): Long = underlying.getLong(columnLabel: String)

  def getHoldability: Int = underlying.getHoldability()

  def refreshRow(): Unit = underlying.refreshRow()

  def getNString(columnIndex: Int): String = underlying.getNString(columnIndex: Int)

  def getNString(columnLabel: String): String = underlying.getNString(columnLabel: String)

  def getConcurrency: Int = underlying.getConcurrency()

  def getFetchSize: Int = underlying.getFetchSize()

  def setFetchSize(rows: Int): Unit = underlying.setFetchSize(rows: Int)

  def getTime(columnIndex: Int): Time = underlying.getTime(columnIndex: Int)

  def getTime(columnLabel: String): Time = underlying.getTime(columnLabel: String)

  def getTime(columnIndex: Int, cal: Calendar): Time = underlying.getTime(columnIndex: Int, cal: Calendar)

  def getTime(columnLabel: String, cal: Calendar): Time = underlying.getTime(columnLabel: String, cal: Calendar)

  def getByte(columnIndex: Int): Byte = underlying.getByte(columnIndex: Int)

  def getByte(columnLabel: String): Byte = underlying.getByte(columnLabel: String)

  def getBoolean(columnIndex: Int): Boolean = underlying.getBoolean(columnIndex: Int)

  def getBoolean(columnLabel: String): Boolean = underlying.getBoolean(columnLabel: String)

  def getFetchDirection(): Int = underlying.getFetchDirection()

  def getAsciiStream(columnIndex: Int): InputStream = underlying.getAsciiStream(columnIndex: Int)

  def getAsciiStream(columnLabel: String): InputStream = underlying.getAsciiStream(columnLabel: String)

  def getObject(columnIndex: Int): AnyRef = underlying.getObject(columnIndex: Int)

  def getObject(columnLabel: String): AnyRef = underlying.getObject(columnLabel: String)

  def getObject(columnIndex: Int, map: util.Map[String, Class[_]]): AnyRef = underlying.getObject(columnIndex: Int, map: util.Map[String, Class[_]])

  def getObject(columnLabel: String, map: util.Map[String, Class[_]]): AnyRef = underlying.getObject(columnLabel: String, map: util.Map[String, Class[_]])

  def getObject[T](columnIndex: Int, `type`: Class[T]): T = underlying.getObject[T](columnIndex: Int, `type`: Class[T])

  def getObject[T](columnLabel: String, `type`: Class[T]): T = underlying.getObject[T](columnLabel: String, `type`: Class[T])

  def getShort(columnIndex: Int): Short = underlying.getShort(columnIndex: Int)

  def getShort(columnLabel: String): Short = underlying.getShort(columnLabel: String)

  def getNCharacterStream(columnIndex: Int): Reader = underlying.getNCharacterStream(columnIndex: Int)

  def getNCharacterStream(columnLabel: String): Reader = underlying.getNCharacterStream(columnLabel: String)

  def close(): Unit = underlying.close()

  def wasNull(): Boolean = underlying.wasNull()

  def getRef(columnIndex: Int): Ref = underlying.getRef(columnIndex: Int)

  def getRef(columnLabel: String): Ref = underlying.getRef(columnLabel: String)

  def isClosed: Boolean = underlying.isClosed()

  def findColumn(columnLabel: String): Int = underlying.findColumn(columnLabel: String)

  def getWarnings: SQLWarning = underlying.getWarnings()

  def getDate(columnIndex: Int): Date = underlying.getDate(columnIndex: Int)

  def getDate(columnLabel: String): Date = underlying.getDate(columnLabel: String)

  def getDate(columnIndex: Int, cal: Calendar): Date = underlying.getDate(columnIndex: Int, cal: Calendar)

  def getDate(columnLabel: String, cal: Calendar): Date = underlying.getDate(columnLabel: String, cal: Calendar)

  def getCursorName: String = underlying.getCursorName()

  def getStatement: Statement = underlying.getStatement()

  def getSQLXML(columnIndex: Int): SQLXML = underlying.getSQLXML(columnIndex: Int)

  def getSQLXML(columnLabel: String): SQLXML = underlying.getSQLXML(columnLabel: String)

  def getInt(columnIndex: Int): Int = underlying.getInt(columnIndex: Int)

  def getInt(columnLabel: String): Int = underlying.getInt(columnLabel: String)

  def getBlob(columnIndex: Int): Blob = underlying.getBlob(columnIndex: Int)

  def getBlob(columnLabel: String): Blob = underlying.getBlob(columnLabel: String)

  def getBytes(columnIndex: Int): Array[Byte] = underlying.getBytes(columnIndex: Int)

  def getBytes(columnLabel: String): Array[Byte] = underlying.getBytes(columnLabel: String)

  def getString(columnIndex: Int): String = underlying.getString(columnIndex: Int)

  def getString(columnLabel: String): String = underlying.getString(columnLabel: String)

}

