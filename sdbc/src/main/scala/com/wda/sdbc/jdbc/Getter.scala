package com.wda.sdbc.jdbc

import java.io.{Reader, InputStream}
import java.sql.{ResultSet, Time, Date, Timestamp}
import java.time.{LocalTime, LocalDate, Instant, LocalDateTime}
import java.util.UUID

import com.wda.sdbc.base

trait JdbcGetter extends base.Getter[ResultSet] {
  self: JdbcRow =>

  trait JdbcGetter[+T] extends Getter[JdbcRow, T] {}

  trait Parser[WrappedRow <: {def getString(columnIndex : Int) : String}, R <: Row, +T] extends Getter[Row, T] {

    override def apply(row: Row, columnIndex: Int): Option[T] = {
      Option(row.underlying.getString(columnIndex)).map(parse)
    }

    def parse(asString: String): T
  }

}

trait LongGetter {
  self: JdbcGetter with JdbcRow =>

  implicit val LongGetter = new JdbcGetter[Long] {

    override def apply(row: JdbcRow, columnIndex: Int): Option[Long] = {
      val i = RowToWrappedRow[java.sql.ResultSet](row).getLong(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait IntGetter {
  self: JdbcGetter with JdbcRow =>

  implicit val IntGetter = new JdbcGetter[Int] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Int] = {
      val i = row.getInt(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait ShortGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val ShortGetter = new JdbcGetter[Short] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Short] = {
      val i = row.getShort(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait ByteGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val ByteGetter = new JdbcGetter[Byte] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Byte] = {
      val i = row.getByte(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait BytesGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val BytesGetter = new JdbcGetter[Array[Byte]] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Array[Byte]] = {
      Option(row.getBytes(columnIndex))
    }
  }
}


trait FloatGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val FloatGetter = new JdbcGetter[Float] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Float] = {
      val i = row.getFloat(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait DoubleGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val DoubleGetter = new JdbcGetter[Double] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Double] = {
      val i = row.getDouble(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait JavaBigDecimalGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val JavaBigDecimalGetter = new JdbcGetter[java.math.BigDecimal] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[java.math.BigDecimal] = {
      Option(row.getBigDecimal(columnIndex))
    }
  }

}

trait ScalaBigDecimalGetter {
  self: JdbcGetter with JdbcRow with JdbcRow with JavaBigDecimalGetter =>

  implicit val ScalaBigDecimalGetter = new JdbcGetter[BigDecimal] {
    override def apply(
      row: JdbcRow,
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
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val TimestampGetter = new JdbcGetter[Timestamp] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Timestamp] = {
      Option(row.getTimestamp(columnIndex))
    }
  }
}

trait DateGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val DateGetter = new JdbcGetter[Date] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Date] = {
      Option(row.underlying.getDate(columnIndex))
    }
  }
}

trait TimeGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val TimeGetter = new JdbcGetter[Time] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Time] = {
      Option(row.getTime(columnIndex))
    }
  }

}

trait LocalDateTimeGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val LocalDateTimeGetter = new JdbcGetter[LocalDateTime] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[LocalDateTime] = {
      Option(row.getTimestamp(columnIndex)).map(_.toLocalDateTime)
    }
  }
}

trait InstantGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val InstantGetter = new JdbcGetter[Instant] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Instant] = {
      Option(row.getTimestamp(columnIndex)).map(_.toInstant)
    }
  }
}

trait LocalDateGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val LocalDateGetter = new JdbcGetter[LocalDate] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[LocalDate] = {
      Option(row.getDate(columnIndex)).map(_.toLocalDate)
    }
  }
}

trait LocalTimeGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val LocalTimeGetter = new JdbcGetter[LocalTime] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[LocalTime] = {
      Option(row.getTime(columnIndex)).map(_.toLocalTime)
    }
  }
}

trait BooleanGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val BooleanGetter = new JdbcGetter[Boolean] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Boolean] = {
      val i = row.getBoolean(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait StringGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val StringGetter = new Parser[java.sql.ResultSet, JdbcRow, String] {
    override def parse(asString: String): String = asString
  }
}

trait UUIDGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val UUIDGetter = new JdbcGetter[UUID] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[UUID] = {
      Option(row.getObject(columnIndex)).
      map(_.asInstanceOf[UUID])
    }
  }
}

trait InputStreamGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val InputStreamGetter = new JdbcGetter[InputStream] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[InputStream] = {
      Option(row.getBinaryStream(columnIndex))
    }
  }

}

trait ReaderGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val ReaderGetter = new JdbcGetter[Reader] {
    override def apply(
      row: JdbcRow,
      columnIndex: Int
    ): Option[Reader] = {
      Option(row.getCharacterStream(columnIndex))
    }
  }
}

trait AnyRefGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  val AnyRefGetter = new JdbcGetter[AnyRef] {
    override def apply(row: JdbcRow, columnIndex: Int): Option[AnyRef] = {
      Option(row.getObject(columnIndex))
    }
  }

}
