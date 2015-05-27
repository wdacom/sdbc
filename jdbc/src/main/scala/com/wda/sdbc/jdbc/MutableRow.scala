package com.wda.sdbc.jdbc

import java.io.{InputStream, Reader}
import java.math.BigDecimal
import java.sql.{Array => JdbcArray, _}
class MutableRow private[jdbc] (underlying: ResultSet)
  extends Row(underlying) {

  def update[T](columnIndex: Int, x: T)(implicit updater: Updater[T]): Unit = {
    updater.update(this, columnIndex, x)
  }

  def updateArray(columnIndex: Int, x: JdbcArray): Unit = underlying.updateArray(columnIndex: Int, x)

  def updateArray(columnLabel: String, x: JdbcArray): Unit = underlying.updateArray(columnLabel: String, x)

  def updateAsciiStream(columnIndex: Int, x: InputStream): Unit = underlying.updateAsciiStream(columnIndex, x)

  def updateAsciiStream(columnLabel: String, x: InputStream): Unit = underlying.updateAsciiStream(columnLabel, x)

  def updateAsciiStream(columnIndex: Int, x: InputStream, length: Int): Unit = underlying.updateAsciiStream(columnIndex, x, length)

  def updateAsciiStream(columnLabel: String, x: InputStream, length: Int): Unit = underlying.updateAsciiStream(columnLabel, x, length)

  def updateAsciiStream(columnIndex: Int, x: InputStream, length: Long): Unit = underlying.updateAsciiStream(columnIndex, x, length)

  def updateAsciiStream(columnLabel: String, x: InputStream, length: Long): Unit = underlying.updateAsciiStream(columnLabel, x, length)

  def updateBigDecimal(columnIndex: Int, x: BigDecimal): Unit = underlying.updateBigDecimal(columnIndex, x)

  def updateBigDecimal(columnLabel: String, x: BigDecimal): Unit = underlying.updateBigDecimal(columnLabel, x)

  def updateBigDecimal(columnIndex: Int, x: scala.BigDecimal): Unit = updateBigDecimal(columnIndex, x.underlying())

  def updateBigDecimal(columnLabel: String, x: scala.BigDecimal): Unit = updateBigDecimal(columnLabel, x.underlying())

  def updateBinaryStream(columnIndex: Int, x: InputStream): Unit = underlying.updateBinaryStream(columnIndex, x)

  def updateBinaryStream(columnLabel: String, x: InputStream): Unit = underlying.updateBinaryStream(columnLabel, x)

  def updateBinaryStream(columnIndex: Int, x: InputStream, length: Int): Unit = underlying.updateBinaryStream(columnIndex, x, length)

  def updateBinaryStream(columnLabel: String, x: InputStream, length: Int): Unit = underlying.updateBinaryStream(columnLabel, x, length)

  def updateBinaryStream(columnIndex: Int, x: InputStream, length: Long): Unit = underlying.updateBinaryStream(columnIndex, x, length)

  def updateBinaryStream(columnLabel: String, x: InputStream, length: Long): Unit = underlying.updateBinaryStream(columnLabel, x, length)

  def updateBlob(columnIndex: Int, x: InputStream, length: Long): Unit = underlying.updateBlob(columnIndex, x, length)

  def updateBlob(columnLabel: String, x: InputStream, length: Long): Unit = underlying.updateBlob(columnLabel, x, length)

  def updateBlob(columnIndex: Int, x: InputStream): Unit = underlying.updateBlob(columnIndex, x)

  def updateBlob(columnLabel: String, x: InputStream): Unit = underlying.updateBlob(columnLabel, x)

  def updateBlob(columnIndex: Int, x: java.sql.Blob): Unit = underlying.updateBlob(columnIndex, x)

  def updateBlob(columnLabel: String, x: java.sql.Blob): Unit = underlying.updateBlob(columnLabel, x)

  def updateBoolean(columnIndex: Int, x: Boolean): Unit = underlying.updateBoolean(columnIndex, x)

  def updateBoolean(columnLabel: String, x: Boolean): Unit = underlying.updateBoolean(columnLabel, x)

  def updateByte(columnIndex: Int, x: Byte): Unit = underlying.updateByte(columnIndex, x)

  def updateByte(columnLabel: String, x: Byte): Unit = underlying.updateByte(columnLabel, x)

  def updateBytes(columnIndex: Int, x: Array[Byte]): Unit = underlying.updateBytes(columnIndex, x)

  def updateBytes(columnLabel: String, x: Array[Byte]): Unit = underlying.updateBytes(columnLabel, x)

  def updateDate(columnIndex: Int, x: java.sql.Date): Unit = underlying.updateDate(columnIndex, x)
  def updateDate(columnLabel: String, x: java.sql.Date): Unit = underlying.updateDate(columnLabel, x)
  def updateDouble(columnIndex: Int, x: Double): Unit = underlying.updateDouble(columnIndex, x)
  def updateDouble(columnLabel: String, x: Double): Unit = underlying.updateDouble(columnLabel, x)
  def updateFloat(columnIndex: Int, x: Float): Unit = underlying.updateFloat(columnIndex, x)
  def updateFloat(columnLabel: String, x: Float): Unit = underlying.updateFloat(columnLabel, x)
  def updateInt(columnIndex: Int, x: Int): Unit = underlying.updateInt(columnIndex, x)
  def updateInt(columnLabel: String, x: Int): Unit = underlying.updateInt(columnLabel, x)
  def updateLong(columnIndex: Int, x: Long): Unit = underlying.updateLong(columnIndex, x)
  def updateLong(columnLabel: String, x: Long): Unit = underlying.updateLong(columnLabel, x)

  def updateNString(columnIndex: Int, nString: String): Unit = underlying.updateNString(columnIndex, nString)
  def updateNString(columnLabel: String, nString: String): Unit = underlying.updateNString(columnLabel, nString)

  def updateRef(columnIndex: Int, x: java.sql.Ref): Unit = underlying.updateRef(columnIndex, x)
  def updateRef(columnLabel: String, x: java.sql.Ref): Unit = underlying.updateRef(columnLabel, x)

  def updateRow(): Unit = underlying.updateRow()
  def insertRow(): Unit = underlying.insertRow()

  def updateRowId(columnIndex: Int, x: RowId): Unit = underlying.updateRowId(columnIndex, x)
  def updateRowId(columnLabel: String, x: RowId): Unit = underlying.updateRowId(columnLabel, x)
  def updateShort(columnIndex: Int, x: Short): Unit = underlying.updateShort(columnIndex, x)
  def updateShort(columnLabel: String, x: Short): Unit = underlying.updateShort(columnLabel, x)
  def updateSQLXML(columnIndex: Int, xmlObject: SQLXML): Unit = underlying.updateSQLXML(columnIndex, xmlObject)
  def updateSQLXML(columnLabel: String, xmlObject: SQLXML): Unit = underlying.updateSQLXML(columnLabel, xmlObject)
  def updateString(columnIndex: Int, x: String): Unit = underlying.updateString(columnIndex, x)
  def updateString(columnLabel: String, x: String): Unit = underlying.updateString(columnLabel, x)
  def updateTime(columnIndex: Int, x: java.sql.Time): Unit = underlying.updateTime(columnIndex, x)
  def updateTime(columnLabel: String, x: java.sql.Time): Unit = underlying.updateTime(columnLabel, x)
  def updateTimestamp(columnIndex: Int, x: java.sql.Timestamp): Unit = underlying.updateTimestamp(columnIndex, x)
  def updateTimestamp(columnLabel: String, x: java.sql.Timestamp): Unit = underlying.updateTimestamp(columnLabel, x)

  def updateCharacterStream(columnIndex: Int, x: Reader, length: Int): Unit = underlying.updateCharacterStream(columnIndex, x, length)
  def updateCharacterStream(columnLabel: String, x: Reader, length: Int): Unit = underlying.updateCharacterStream(columnLabel, x, length)
  def updateCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = underlying.updateCharacterStream(columnIndex, x, length)
  def updateCharacterStream(columnLabel: String, x: Reader, length: Long): Unit = underlying.updateCharacterStream(columnLabel, x, length)
  def updateCharacterStream(columnIndex: Int, x: Reader): Unit = underlying.updateCharacterStream(columnIndex, x)
  def updateCharacterStream(columnLabel: String, x: Reader): Unit = underlying.updateCharacterStream(columnLabel, x)

  def updateNCharacterStream(columnIndex: Int, x: Reader, length: Int): Unit = underlying.updateNCharacterStream(columnIndex, x, length)
  def updateNCharacterStream(columnLabel: String, x: Reader, length: Int): Unit = underlying.updateNCharacterStream(columnLabel, x, length)
  def updateNCharacterStream(columnIndex: Int, x: Reader, length: Long): Unit = underlying.updateNCharacterStream(columnIndex, x, length)
  def updateNCharacterStream(columnLabel: String, x: Reader, length: Long): Unit = underlying.updateNCharacterStream(columnLabel, x, length)
  def updateNCharacterStream(columnIndex: Int, x: Reader): Unit = underlying.updateNCharacterStream(columnIndex, x)
  def updateNCharacterStream(columnLabel: String, x: Reader): Unit = underlying.updateNCharacterStream(columnLabel, x)

  def updateClob(columnIndex: Int, x: Clob, length: Int): Unit = underlying.updateClob(columnIndex, x)
  def updateClob(columnLabel: String, x: Clob, length: Int): Unit = underlying.updateClob(columnLabel, x)
  def updateClob(columnIndex: Int, x: Reader, length: Long): Unit = underlying.updateClob(columnIndex, x, length)
  def updateClob(columnLabel: String, x: Reader, length: Long): Unit = underlying.updateClob(columnLabel, x, length)
  def updateClob(columnIndex: Int, x: Reader): Unit = underlying.updateClob(columnIndex, x)
  def updateClob(columnLabel: String, x: Reader): Unit = underlying.updateClob(columnLabel, x)

  def updateNClob(columnIndex: Int, x: NClob, length: Int): Unit = underlying.updateNClob(columnIndex, x)
  def updateNClob(columnLabel: String, x: NClob, length: Int): Unit = underlying.updateNClob(columnLabel, x)
  def updateNClob(columnIndex: Int, x: Reader, length: Long): Unit = underlying.updateNClob(columnIndex, x, length)
  def updateNClob(columnLabel: String, x: Reader, length: Long): Unit = underlying.updateNClob(columnLabel, x, length)
  def updateNClob(columnIndex: Int, x: Reader): Unit = underlying.updateNClob(columnIndex, x)
  def updateNClob(columnLabel: String, x: Reader): Unit = underlying.updateNClob(columnLabel, x)

  def updateObject(columnIndex: Int, x: AnyRef): Unit = underlying.updateObject(columnIndex, x)
  def updateObject(columnIndex: Int, x: AnyRef, scaleOrLength: Int): Unit = underlying.updateObject(columnIndex, x, scaleOrLength)
  def updateObject(columnIndex: Int, x: AnyRef, targetSqlType: SQLType): Unit = underlying.updateObject(columnIndex, x, targetSqlType)
  def updateObject(columnIndex: Int, x: AnyRef, targetSqlType: SQLType, scaleOrLength: Int): Unit = underlying.updateObject(columnIndex, targetSqlType, scaleOrLength)
  def updateObject(columnLabel: String, x: AnyRef): Unit = underlying.updateObject(columnLabel, x)
  def updateObject(columnIndex: String, x: AnyRef, scaleOrLength: Int): Unit = underlying.updateObject(columnIndex, x, scaleOrLength)
  def updateObject(columnIndex: String, x: AnyRef, targetSqlType: SQLType): Unit = underlying.updateObject(columnIndex, x, targetSqlType)
  def updateObject(columnIndex: String, x: AnyRef, targetSqlType: SQLType, scaleOrLength: Int): Unit = underlying.updateObject(columnIndex, targetSqlType, scaleOrLength)

}
