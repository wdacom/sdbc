package com.wda.sdbc.base

import java.io.{InputStream, Reader}
import java.sql.{Array => _, _}
import java.time._
import java.util.UUID

trait Getter {
  self: Row =>

  trait Getter[+T] extends Function[Row, Option[T]] {

    override def apply(row: Row): Option[T] = {
      apply(row, 1)
    }

    def apply(row: Row, columnName: String): Option[T] = {
      val columnIndex = row.columnIndexes(columnName)
      apply(row, columnIndex)
    }

    def apply(row: Row, columnIndex: Int): Option[T]

  }

  trait Parser[+T] extends Getter[T] {

    override def apply(row: Row, columnIndex: Int): Option[T] = {
      Option(row.underlying.getString(columnIndex)).map(parse)
    }

    def parse(asString: String): T

  }

}

trait LongGetter {
  self: Getter with Row =>

  implicit val LongGetter = new Getter[Long] {
    override def apply(row: Row, columnIndex: Int): Option[Long] = {
      val i = row.getLong(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait IntGetter {
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row with JavaBigDecimalGetter =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

  implicit val DateGetter = new Getter[Date] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[Date] = {
      Option(row.underlying.getDate(columnIndex))
    }
  }
}

trait TimeGetter {
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

  implicit val StringGetter = new Parser[String] {
    override def parse(asString: String): String = asString
  }
}

trait UUIDGetter {
  self: Getter with Row =>

  implicit val UUIDGetter = new Getter[UUID] {
    override def apply(
      row: Row,
      columnIndex: Int
    ): Option[UUID] = {
      Option(row.getString(columnIndex)).map(UUID.fromString)
    }
  }
}

trait InputStreamGetter {
  self: Getter with Row =>

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
  self: Getter with Row =>

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
  self: Getter with Row =>

  val AnyRefGetter = new Getter[AnyRef] {
    override def apply(row: Row, columnIndex: Int): Option[AnyRef] = {
      Option(row.getObject(columnIndex))
    }
  }

}
