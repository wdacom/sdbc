package com.rocketfuel.sdbc.base.jdbc

import java.io.{InputStream, Reader}
import java.math.BigDecimal
import java.net.URL
import java.sql.{Array => JdbcArray, _}
import com.rocketfuel.sdbc.base

case class ImmutableRow private[jdbc] (
  override val columnTypes: IndexedSeq[String],
  override val columnNames: IndexedSeq[String],
  parameters: IndexedSeq[Option[Any]],
  override val getMetaData: ResultSetMetaData,
  override val getRow: Int
) extends Row {

  override def asIntMap(implicit getter: Getter[ParameterValue]): IndexedSeq[Option[Any]] = {
    parameters
  }

  private var _wasNull = false

  override def wasNull: Boolean = _wasNull

  private def incorrectType(): Nothing = {
    throw new SQLException("Incorrect type")
  }

  private def setWasNull(columnIndexes: Int): Option[Any] = {
    val parameter = parameters(columnIndexes)
    _wasNull = parameter.isEmpty
    parameter
  }

  override def getString(columnIndexes: Int): String = {
    setWasNull(columnIndexes) map { case s: String => s case _ => incorrectType()} orNull
  }

  override def getString(columnLabel: String): String = {
    getString(columnIndexes(columnLabel))
  }

  override def getLong(columnIndexes: Int): Long = {
    setWasNull(columnIndexes) map { case s: Long => s case _ => incorrectType()} getOrElse 0L
  }

  override def getLong(columnLabel: String): Long = getLong(columnIndexes(columnLabel))

  override def getTimestamp(columnIndexes: Int): Timestamp = {
    setWasNull(columnIndexes) map { case s: Timestamp => s case _ => incorrectType()} orNull
  }

  override def getTimestamp(columnLabel: String): Timestamp = getTimestamp(columnIndexes(columnLabel))

  override def getBinaryStream(columnIndexes: Int): InputStream = {
    setWasNull(columnIndexes) map { case s: InputStream => s case _ => incorrectType()} orNull
  }

  override def getBinaryStream(columnLabel: String): InputStream = getBinaryStream(columnIndexes(columnLabel))

  override def getDouble(columnIndexes: Int): Double = {
    setWasNull(columnIndexes) map { case s: Double => s case _ => incorrectType()} getOrElse 0.0
  }

  override def getDouble(columnLabel: String): Double = getDouble(columnIndexes(columnLabel))

  override def getCharacterStream(columnIndexes: Int): Reader = {
    setWasNull(columnIndexes) map { case s: Reader => s case _ => incorrectType()} orNull
  }

  override def getCharacterStream(columnLabel: String): Reader = getCharacterStream(columnIndexes(columnLabel))

  override def getArray(columnIndexes: Int): JdbcArray = {
    setWasNull(columnIndexes) map { case s: JdbcArray => s case _ => incorrectType()} orNull
  }

  override def getArray(columnLabel: String): JdbcArray = getArray(columnIndexes(columnLabel))

  override def getURL(columnIndexes: Int): URL = {
    setWasNull(columnIndexes) map { case s: URL => s case _ => incorrectType()} orNull
  }

  override def getURL(columnLabel: String): URL = getURL(columnIndexes(columnLabel))

  override def getBigDecimal(columnIndexes: Int): BigDecimal = {
    setWasNull(columnIndexes) map { case s: BigDecimal => s case _ => incorrectType()} orNull
  }

  override def getBigDecimal(columnLabel: String): BigDecimal = getBigDecimal(columnIndexes(columnLabel))

  override def getFloat(columnIndexes: Int): Float = {
    setWasNull(columnIndexes) map { case s: Float => s case _ => incorrectType()} getOrElse 0F
  }

  override def getFloat(columnLabel: String): Float = getFloat(columnIndexes(columnLabel))

  override def getTime(columnIndexes: Int): Time = {
    setWasNull(columnIndexes) map { case s: Time => s case _ => incorrectType()} orNull
  }

  override def getTime(columnLabel: String): Time = getTime(columnIndexes(columnLabel))

  override def getByte(columnIndexes: Int): Byte = {
    setWasNull(columnIndexes) map { case s: Byte => s case _ => incorrectType()} getOrElse 0
  }

  override def getByte(columnLabel: String): Byte = getByte(columnIndexes(columnLabel))

  override def getBoolean(columnIndexes: Int): Boolean = {
    setWasNull(columnIndexes).contains(true)
  }

  override def getBoolean(columnLabel: String): Boolean = getBoolean(columnIndexes(columnLabel))

  override def getShort(columnIndexes: Int): Short = {
    setWasNull(columnIndexes) map { case s: Short => s case _ => incorrectType()} getOrElse 0
  }

  override def getShort(columnLabel: String): Short = getShort(columnIndexes(columnLabel))

  override def getDate(columnIndexes: Int): Date = {
    setWasNull(columnIndexes) map { case s: Date => s case _ => incorrectType()} orNull
  }

  override def getDate(columnLabel: String): Date = getDate(columnIndexes(columnLabel))

  override def getSQLXML(columnIndexes: Int): SQLXML = {
    setWasNull(columnIndexes) map { case s: SQLXML => s case _ => incorrectType()} orNull
  }

  override def getSQLXML(columnLabel: String): SQLXML = getSQLXML(columnIndexes(columnLabel))

  override def getInt(columnIndexes: Int): Int = {
    setWasNull(columnIndexes) map { case s: Int => s case _ => incorrectType()} getOrElse(0)
  }

  override def getInt(columnLabel: String): Int = getInt(columnIndexes(columnLabel))

  override def getBlob(columnIndexes: Int): Blob = {
    setWasNull(columnIndexes) map { case s: Blob => s case _ => incorrectType()} orNull
  }

  override def getBlob(columnLabel: String): Blob = getBlob(columnIndexes(columnLabel))

  override def getBytes(columnIndexes: Int): Array[Byte] = {
    setWasNull(columnIndexes) map { case s: Array[Byte] => s case _ => incorrectType()} orNull
  }

  override def getBytes(columnLabel: String): Array[Byte] = getBytes(columnIndexes(columnLabel))

  override def getObject(columnIndexes: Int): AnyRef = {
    setWasNull(columnIndexes) map base.box orNull
  }

  override def getObject(columnLabel: String): AnyRef = {
    getObject(columnIndexes(columnLabel))
  }

  override def getClob(columnIndexes: Int): Clob = {
    throw new UnsupportedOperationException()
  }

  override def getClob(columnLabel: String): Clob = {
    getClob(columnIndexes(columnLabel))
  }
}

object ImmutableRow {
  implicit def apply(row: MutableRow)(implicit getter: Getter[ParameterValue]): ImmutableRow = {
    ImmutableRow(
      columnTypes = row.columnTypes,
      columnNames = row.columnNames,
      parameters = row.asIntMap,
      getMetaData = row.getMetaData,
      getRow = row.getRow
    )
  }
}
