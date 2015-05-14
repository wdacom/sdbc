package com.wda.sdbc.jdbc

import java.io.{Reader, InputStream}
import java.sql.{Time, Date, Timestamp}
import java.time.{LocalTime, LocalDate, Instant, LocalDateTime}
import java.util.UUID

import com.wda.sdbc.base

trait JdbcGetter extends base.Getter[java.sql.ResultSet] {
  self: JdbcRow =>

  trait Parser[+T] extends Getter[T] {

    override def apply(row: UnderlyingRow, columnIndex: Int): Option[T] = {
      Option(row.getString(columnIndex)).map(parse)
    }

    def parse(asString: String): T
  }

}

trait LongGetter {
  self: JdbcGetter with JdbcRow =>

  implicit val LongGetter = new Getter[Long] {

    override def apply(row: UnderlyingRow, columnIndex: Int): Option[Long] = {
      val i = row.getLong(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }

  }

}

trait IntGetter {
  self: JdbcGetter with JdbcRow =>

  implicit val IntGetter = new Getter[Int] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val ShortGetter = new Getter[Short] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val ByteGetter = new Getter[Byte] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val BytesGetter = new Getter[Array[Byte]] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[Array[Byte]] = {
      Option(row.getBytes(columnIndex))
    }
  }
}


trait FloatGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val FloatGetter = new Getter[Float] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val DoubleGetter = new Getter[Double] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val JavaBigDecimalGetter = new Getter[java.math.BigDecimal] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[java.math.BigDecimal] = {
      Option(row.getBigDecimal(columnIndex))
    }
  }

}

trait ScalaBigDecimalGetter {
  self: JdbcGetter with JdbcRow with JdbcRow with JavaBigDecimalGetter =>

  implicit val ScalaBigDecimalGetter = new Getter[BigDecimal] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val TimestampGetter = new Getter[Timestamp] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[Timestamp] = {
      Option(row.getTimestamp(columnIndex))
    }
  }
}

trait DateGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val DateGetter = new Getter[Date] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[Date] = {
      Option(row.underlying.getDate(columnIndex))
    }
  }
}

trait TimeGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val TimeGetter = new Getter[Time] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[Time] = {
      Option(row.getTime(columnIndex))
    }
  }

}

trait LocalDateTimeGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val LocalDateTimeGetter = new Getter[LocalDateTime] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[LocalDateTime] = {
      Option(row.getTimestamp(columnIndex)).map(_.toLocalDateTime)
    }
  }
}

trait InstantGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val InstantGetter = new Getter[Instant] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[Instant] = {
      Option(row.getTimestamp(columnIndex)).map(_.toInstant)
    }
  }
}

trait LocalDateGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val LocalDateGetter = new Getter[LocalDate] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[LocalDate] = {
      Option(row.getDate(columnIndex)).map(_.toLocalDate)
    }
  }
}

trait LocalTimeGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val LocalTimeGetter = new Getter[LocalTime] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[LocalTime] = {
      Option(row.getTime(columnIndex)).map(_.toLocalTime)
    }
  }
}

trait BooleanGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val BooleanGetter = new Getter[Boolean] {
    override def apply(
      row: UnderlyingRow,
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

  implicit val StringGetter = new Parser[String] {

    override def apply(row: UnderlyingRow, columnIndex: Int): Option[String] = {
      Option(row.getString(columnIndex))
    }

    override def parse(asString: String): String = asString
  }
}

trait UUIDGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val UUIDGetter = new Getter[UUID] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[UUID] = {
      Option(row.getObject(columnIndex)).
      map(_.asInstanceOf[UUID])
    }
  }
}

trait InputStreamGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val InputStreamGetter = new Getter[InputStream] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[InputStream] = {
      Option(row.getBinaryStream(columnIndex))
    }
  }

}

trait ReaderGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  implicit val ReaderGetter = new Getter[Reader] {
    override def apply(
      row: UnderlyingRow,
      columnIndex: Int
    ): Option[Reader] = {
      Option(row.getCharacterStream(columnIndex))
    }
  }
}

trait AnyRefGetter {
  self: JdbcGetter with JdbcRow with JdbcRow =>

  val AnyRefGetter = new Getter[AnyRef] {
    override def apply(row: UnderlyingRow, columnIndex: Int): Option[AnyRef] = {
      Option(row.getObject(columnIndex))
    }
  }

}
