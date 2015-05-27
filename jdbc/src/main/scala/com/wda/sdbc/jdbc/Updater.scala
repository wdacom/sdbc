package com.wda.sdbc.jdbc

import java.sql.{Time, Date, Timestamp}
import java.io.{InputStream, Reader}
import java.time.{LocalTime, LocalDate, Instant, LocalDateTime}
import java.util.UUID

trait Updater[T] {

  def update(row: MutableRow, columnIndex: Int, x: T): Unit

  def update(row: MutableRow, columnLabel: String, x: T): Unit = {
    val columnIndex = row.columnIndexes(columnLabel)
    update(row, columnIndex, x)
  }

}

trait LongUpdater {
  implicit val LongUpdater = new Updater[Long] {
    override def update(row: MutableRow, columnIndex: Int, x: Long): Unit = {
      row.updateLong(columnIndex, x)
    }
  }
}

trait IntUpdater {
  implicit val IntUpdater = new Updater[Int] {
    override def update(row: MutableRow, columnIndex: Int, x: Int): Unit = {
      row.updateInt(columnIndex, x)
    }
  }
}

trait ShortUpdater {
  implicit val ShortUpdater = new Updater[Short] {
    override def update(row: MutableRow, columnIndex: Int, x: Short): Unit = {
      row.updateShort(columnIndex, x)
    }
  }
}

trait ByteUpdater {
  implicit val ByteUpdater = new Updater[Byte] {
    override def update(row: MutableRow, columnIndex: Int, x: Byte): Unit = {
      row.updateByte(columnIndex, x)
    }
  }
}

trait BytesUpdater {
  implicit val BytesUpdater = new Updater[Array[Byte]] {
    override def update(row: MutableRow, columnIndex: Int, x: Array[Byte]): Unit = {
      row.updateBytes(columnIndex, x)
    }
  }
}

trait DoubleUpdater {
  implicit val DoubleUpdater = new Updater[Double] {
    override def update(row: MutableRow, columnIndex: Int, x: Double): Unit = {
      row.updateDouble(columnIndex, x)
    }
  }
}

trait FloatUpdater {
  implicit val FloatUpdater = new Updater[Float] {
    override def update(row: MutableRow, columnIndex: Int, x: Float): Unit = {
      row.updateFloat(columnIndex, x)
    }
  }
}

trait JavaBigDecimalUpdater {
  implicit val JavaBigDecimalUpdater = new Updater[java.math.BigDecimal] {
    override def update(row: MutableRow, columnIndex: Int, x: java.math.BigDecimal): Unit = {
      row.updateBigDecimal(columnIndex, x)
    }
  }
}

trait ScalaBigDecimalUpdater {
  implicit val ScalaBigDecimalUpdater = new Updater[BigDecimal] {
    override def update(row: MutableRow, columnIndex: Int, x: BigDecimal): Unit = {
      row.updateBigDecimal(columnIndex, x)
    }
  }
}

trait TimestampUpdater {
  implicit val TimestampUpdater = new Updater[Timestamp] {
    override def update(row: MutableRow, columnIndex: Int, x: Timestamp): Unit = {
      row.updateTimestamp(columnIndex, x)
    }
  }
}

trait DateUpdater {
  implicit val DateUpdater = new Updater[Date] {
    override def update(row: MutableRow, columnIndex: Int, x: Date): Unit = {
      row.updateDate(columnIndex, x)
    }
  }
}

trait TimeUpdater {
  implicit val TimeUpdater = new Updater[Time] {
    override def update(row: MutableRow, columnIndex: Int, x: Time): Unit = {
      row.updateTime(columnIndex, x)
    }
  }
}

trait LocalDateTimeUpdater {
  implicit val LocalDateTimeUpdater = new Updater[LocalDateTime] {
    override def update(row: MutableRow, columnIndex: Int, x: LocalDateTime): Unit = {
      row.updateTimestamp(columnIndex, Timestamp.valueOf(x))
    }
  }
}

trait InstantUpdater {
  implicit val InstantUpdater = new Updater[Instant] {
    override def update(row: MutableRow, columnIndex: Int, x: Instant): Unit = {
      row.updateTimestamp(columnIndex, Timestamp.from(x))
    }
  }
}

trait LocalDateUpdater {
  implicit val LocalDateUpdater = new Updater[LocalDate] {
    override def update(row: MutableRow, columnIndex: Int, x: LocalDate): Unit = {
      row.updateDate(columnIndex, Date.valueOf(x))
    }
  }
}

trait LocalTimeUpdater {
  implicit val LocalTimeUpdater = new Updater[LocalTime] {
    override def update(row: MutableRow, columnIndex: Int, x: LocalTime): Unit = {
      row.updateTime(columnIndex, Time.valueOf(x))
    }
  }
}

trait BooleanUpdater {
  implicit val BooleanUpdater = new Updater[Boolean] {
    override def update(row: MutableRow, columnIndex: Int, x: Boolean): Unit = {
      row.updateBoolean(columnIndex, x)
    }
  }
}

trait StringUpdater {
  implicit val StringUpdater = new Updater[String] {
    override def update(row: MutableRow, columnIndex: Int, x: String): Unit = {
      row.updateString(columnIndex, x)
    }
  }
}

trait UUIDUpdater {
  implicit val UUIDUpdater = new Updater[UUID] {
    override def update(row: MutableRow, columnIndex: Int, x: UUID): Unit = {
      row.updateObject(columnIndex, x)
    }
  }
}


trait InputStreamUpdater {
  implicit val InputStreamUpdater = new Updater[InputStream] {
    override def update(row: MutableRow, columnIndex: Int, x: InputStream): Unit = {
      row.updateBinaryStream(columnIndex, x)
    }
  }
}

trait UpdateReader {
  implicit val ReaderUpdater = new Updater[Reader] {
    override def update(row: MutableRow, columnIndex: Int, x: Reader): Unit = {
      row.updateCharacterStream(columnIndex, x)
    }
  }
}

trait AnyRefUpdater {
  val AnyRefUpdater = new Updater[AnyRef] {
    override def update(row: MutableRow, columnIndex: Int, x: AnyRef): Unit = {
      row.updateObject(columnIndex, x)
    }
  }
}
