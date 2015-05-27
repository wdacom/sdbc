package com.wda.sdbc.jdbc

import java.io.{InputStream, Reader}
import java.sql.{Date, Time, Timestamp}
import java.time.{Instant, LocalDate, LocalDateTime, LocalTime}
import java.util.UUID

import com.wda.sdbc.base

trait Getter[+T] extends base.Getter[Row, Int, T]{

  override def indexOne: Int = 1

  def apply(row: Row, columnName: String): Option[T] = {
    Getter.findColumnIndex(row, columnName).flatMap { columnIndex =>
      implicit val getter = this
      apply(row, columnIndex)
    }
  }

}

object Getter {
  def findColumnIndex(row: Row, columnName: String): Option[Int] = {
    0.until(row.getMetaData.getColumnCount).find { index =>
      val columnNameAtIndex = row.getMetaData.getColumnName(index)
      columnName == columnNameAtIndex
    }
  }
}

trait Parser[+T] extends Getter[T] {

  override def apply(row: Row, columnIndex: Int): Option[T] = {
    Option(row.getString(columnIndex)).map(parse)
  }

  def parse(asString: String): T

}

trait LongGetter {

  implicit val LongGetter = new Getter[Long] {

    override def apply(row: Row, columnIndex: Int): Option[Long] = {
      val i = row.getLong(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }

  }

}

trait IntGetter {

  implicit val IntGetter = new Getter[Int] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Int] = {
      val i = row.getInt(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait ShortGetter {

  implicit val ShortGetter = new Getter[Short] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Short] = {
      val i = row.getShort(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait ByteGetter {

  implicit val ByteGetter = new Getter[Byte] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Byte] = {
      val i = row.getByte(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait BytesGetter {

  implicit val BytesGetter = new Getter[Array[Byte]] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Array[Byte]] = {
      Option(row.getBytes(columnIndex))
    }
  }
}

trait FloatGetter {

  implicit val FloatGetter = new Getter[Float] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Float] = {
      val i = row.getFloat(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait DoubleGetter {

  implicit val DoubleGetter = new Getter[Double] {

    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Double] = {
      val i = row.getDouble(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait JavaBigDecimalGetter {

  implicit val JavaBigDecimalGetter = new Getter[java.math.BigDecimal] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[java.math.BigDecimal] = {
      Option(row.getBigDecimal(columnIndex))
    }
  }

}

trait ScalaBigDecimalGetter {
  self: JavaBigDecimalGetter =>

  implicit val ScalaBigDecimalGetter = new Getter[BigDecimal] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[BigDecimal] = {
      JavaBigDecimalGetter(
        row,
        columnIndex
      ).map(BigDecimal.apply)
    }
  }
}

trait TimestampGetter {

  implicit val TimestampGetter = new Getter[Timestamp] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Timestamp] = {
      Option(row.getTimestamp(columnIndex))
    }
  }
}

trait DateGetter {

  implicit val DateGetter = new Getter[Date] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Date] = {
      Option(row.getDate(columnIndex))
    }
  }
}

trait TimeGetter {

  implicit val TimeGetter = new Getter[Time] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Time] = {
      Option(row.getTime(columnIndex))
    }
  }

}

trait LocalDateTimeGetter {

  implicit val LocalDateTimeGetter = new Getter[LocalDateTime] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[LocalDateTime] = {
      Option(row.getTimestamp(columnIndex)).map(_.toLocalDateTime)
    }
  }
}

trait InstantGetter {

  implicit val InstantGetter = new Getter[Instant] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Instant] = {
      Option(row.getTimestamp(columnIndex)).map(_.toInstant)
    }
  }
}

trait LocalDateGetter {

  implicit val LocalDateGetter = new Getter[LocalDate] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[LocalDate] = {
      Option(row.getDate(columnIndex)).map(_.toLocalDate)
    }
  }
}

trait LocalTimeGetter {

  implicit val LocalTimeGetter = new Getter[LocalTime] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[LocalTime] = {
      Option(row.getTime(columnIndex)).map(_.toLocalTime)
    }
  }
}

trait BooleanGetter {

  implicit val BooleanGetter = new Getter[Boolean] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Boolean] = {
      val i = row.getBoolean(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait StringGetter {

  implicit val StringGetter = new Parser[String] {

    override def apply(row: Row, columnIndex: Int): Option[String] = {
      Option(row.getString(columnIndex))
    }

    override def parse(asString: String): String = asString
  }
}

trait UUIDGetter {

  implicit val UUIDGetter = new Getter[UUID] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[UUID] = {
      Option(row.getObject(columnIndex)).
      map(_.asInstanceOf[UUID])
    }
  }
}

trait InputStreamGetter {

  implicit val InputStreamGetter = new Getter[InputStream] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[InputStream] = {
      Option(row.getBinaryStream(columnIndex))
    }
  }

}

trait ReaderGetter {

  implicit val ReaderGetter = new Getter[Reader] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Reader] = {
      Option(row.getCharacterStream(columnIndex))
    }
  }
}

trait AnyRefGetter {

  val AnyRefGetter = new Getter[AnyRef] {
    override def apply(row: Row, columnIndex: Int): Option[AnyRef] = {
      Option(row.getObject(columnIndex))
    }
  }

}
