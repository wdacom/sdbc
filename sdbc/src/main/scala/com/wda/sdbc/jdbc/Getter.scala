package com.wda.sdbc.jdbc

import java.io.{Reader, InputStream}
import java.sql.{ResultSet, Time, Date, Timestamp}
import java.time.{LocalTime, LocalDate, Instant, LocalDateTime}
import java.util.UUID

import com.wda.sdbc.base
import com.wda.sdbc.base.Row

trait JdbcGetter[+T] extends base.Getter[ResultSet, T]

trait Parser[+T] extends JdbcGetter[T] {

  override def apply(row: ResultSet, columnIndex: Int)(implicit isRow: Row[ResultSet]): Option[T] = {
    Option(row.getString(columnIndex)).map(parse)
  }

  def parse(asString: String): T
}

trait LongGetter {

  implicit val LongGetter = new JdbcGetter[Long] {

    override def apply(row: ResultSet, columnIndex: Int)(implicit isRow: Row[ResultSet]): Option[Long] = {
      val i = row.getLong(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }

  }

}

trait IntGetter {

  implicit val IntGetter = new JdbcGetter[Int] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    )(implicit isRow: Row[ResultSet]): Option[Int] = {
      val i = row.getInt(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait ShortGetter {

  implicit val ShortGetter = new JdbcGetter[Short] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    )(implicit isRow: Row[ResultSet]): Option[Short] = {
      val i = row.getShort(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }

}

trait ByteGetter {

  implicit val ByteGetter = new JdbcGetter[Byte] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    )(implicit isRow: Row[ResultSet]): Option[Byte] = {
      val i = row.getByte(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait BytesGetter {

  implicit val BytesGetter = new JdbcGetter[Array[Byte]] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    )(implicit isRow: Row[ResultSet]): Option[Array[Byte]] = {
      Option(row.getBytes(columnIndex))
    }
  }
}


trait FloatGetter {

  implicit val FloatGetter = new JdbcGetter[Float] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    )(implicit isRow: Row[ResultSet]): Option[Float] = {
      val i = row.getFloat(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait DoubleGetter {

  implicit val DoubleGetter = new JdbcGetter[Double] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[Double] = {
      val i = row.getDouble(columnIndex)
      if (row.wasNull()) None
      else Some(i)
    }
  }
}

trait JavaBigDecimalGetter {

  implicit val JavaBigDecimalGetter = new JdbcGetter[java.math.BigDecimal] {
    override def apply(
      row: ResultSet,
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
      row: ResultSet,
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

  implicit val TimestampGetter = new JdbcGetter[Timestamp] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[Timestamp] = {
      Option(row.getTimestamp(columnIndex))
    }
  }
}

trait DateGetter {

  implicit val DateGetter = new JdbcGetter[Date] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[Date] = {
      Option(row.underlying.getDate(columnIndex))
    }
  }
}

trait TimeGetter {

  implicit val TimeGetter = new JdbcGetter[Time] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[Time] = {
      Option(row.getTime(columnIndex))
    }
  }

}

trait LocalDateTimeGetter {

  implicit val LocalDateTimeGetter = new JdbcGetter[LocalDateTime] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[LocalDateTime] = {
      Option(row.getTimestamp(columnIndex)).map(_.toLocalDateTime)
    }
  }
}

trait InstantGetter {

  implicit val InstantGetter = new JdbcGetter[Instant] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[Instant] = {
      Option(row.getTimestamp(columnIndex)).map(_.toInstant)
    }
  }
}

trait LocalDateGetter {

  implicit val LocalDateGetter = new JdbcGetter[LocalDate] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[LocalDate] = {
      Option(row.getDate(columnIndex)).map(_.toLocalDate)
    }
  }
}

trait LocalTimeGetter {

  implicit val LocalTimeGetter = new JdbcGetter[LocalTime] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[LocalTime] = {
      Option(row.getTime(columnIndex)).map(_.toLocalTime)
    }
  }
}

trait BooleanGetter {

  implicit val BooleanGetter = new JdbcGetter[Boolean] {
    override def apply(
      row: ResultSet,
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

    override def apply(row: ResultSet, columnIndex: Int): Option[String] = {
      Option(row.getString(columnIndex))
    }

    override def parse(asString: String): String = asString
  }
}

trait UUIDGetter {

  implicit val UUIDGetter = new JdbcGetter[UUID] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[UUID] = {
      Option(row.getObject(columnIndex)).
      map(_.asInstanceOf[UUID])
    }
  }
}

trait InputStreamGetter {

  implicit val InputStreamGetter = new JdbcGetter[InputStream] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[InputStream] = {
      Option(row.getBinaryStream(columnIndex))
    }
  }

}

trait ReaderGetter {

  implicit val ReaderGetter = new JdbcGetter[Reader] {
    override def apply(
      row: ResultSet,
      columnIndex: Int
    ): Option[Reader] = {
      Option(row.getCharacterStream(columnIndex))
    }
  }
}

trait AnyRefGetter {

  val AnyRefGetter = new JdbcGetter[AnyRef] {
    override def apply(row: ResultSet, columnIndex: Int): Option[AnyRef] = {
      Option(row.getObject(columnIndex))
    }
  }

}
