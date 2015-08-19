package com.rocketfuel.sdbc.base.jdbc

import java.io.{InputStream, Reader}
import java.sql.{SQLException, Date, Time, Timestamp}
import java.time._
import java.util.UUID

import com.rocketfuel.sdbc.base

object Getter {
  def fromValGetter[T <: AnyVal](valGetter: Row => Int => T): Getter[T] = {
    new Getter[T] {
      override def apply(row: Row, ix: Index): Option[T] = {
        val value = valGetter(row)(ix(row))
        if (row.wasNull()) None
        else Some(value)
      }
    }
  }
}

trait Parser[+T] extends Getter[T] {

  override def apply(row: Row, ix: Index): Option[T] = {
    Option(row.getString(ix(row))).map(parse)
  }

  def parse(asString: String): T

}

trait LongGetter {

  implicit val LongGetter =
    Getter.fromValGetter[Long]{ row => ix => row.getLong(ix) }

}

trait IntGetter {

  implicit val IntGetter =
    Getter.fromValGetter[Int]{ row => ix => row.getInt(ix) }

}

trait ShortGetter {

  implicit val ShortGetter =
    Getter.fromValGetter[Short]{ row => ix => row.getShort(ix) }

}

trait ByteGetter {

  implicit val ByteGetter =
    Getter.fromValGetter[Byte]{ row => ix => row.getByte(ix) }
}

trait BytesGetter {

  implicit val BytesGetter =
    new Getter[Array[Byte]] {
      override def apply(row: Row, ix: Index): Option[Array[Byte]] = {
        Option(row.getBytes(ix(row)))
      }
    }
}

trait FloatGetter {

  implicit val FloatGetter =
    Getter.fromValGetter[Float]{ row => ix => row.getFloat(ix) }
}

trait DoubleGetter {

  implicit val DoubleGetter =
    Getter.fromValGetter[Double]{ row => ix => row.getDouble(ix) }
}

trait JavaBigDecimalGetter {

  implicit val JavaBigDecimalGetter = new Getter[java.math.BigDecimal] {
    override def apply(row: Row, ix: Index): Option[java.math.BigDecimal] = {
      Option(row.getBigDecimal(ix(row)))
    }
  }

}

trait ScalaBigDecimalGetter {
  implicit val ScalaBigDecimalGetter = new Getter[BigDecimal] {
    override def apply(row: Row, ix: Index): Option[BigDecimal] = {
      Option(row.getBigDecimal(ix(row))).map(x => x)
    }
  }
}

trait TimestampGetter {

  implicit val TimestampGetter = new Getter[Timestamp] {
    override def apply(row: Row, ix: Index): Option[Timestamp] = {
      Option(row.getTimestamp(ix(row)))
    }
  }
}

trait DateGetter {

  implicit val DateGetter = new Getter[Date] {
    override def apply(row: Row, ix: Index): Option[Date] = {
      Option(row.getDate(ix(row)))
    }
  }
}

trait TimeGetter {

  implicit val TimeGetter = new Getter[Time] {
    override def apply(row: Row, ix: Index): Option[Time] = {
      Option(row.getTime(ix(row)))
    }
  }

}

trait LocalDateTimeGetter {

  implicit val LocalDateTimeGetter = new Getter[LocalDateTime] {
      override def apply(row: Row, ix: Index): Option[LocalDateTime] = {
        Option(row.getTimestamp(ix(row))).map(_.toLocalDateTime)
      }
  }

}

trait InstantGetter {

  implicit val InstantGetter = new Getter[Instant] {
    override def apply(row: Row, ix: Index): Option[Instant] = {
      Option(row.getTimestamp(ix(row))).map(_.toInstant)
    }
  }

}

trait LocalDateGetter {

  implicit val LocalDateGetter = new Getter[LocalDate] {
    override def apply(row: Row, ix: Index): Option[LocalDate] = {
      Option(row.getDate(ix(row))).map(_.toLocalDate)
    }
  }

}

trait LocalTimeGetter {

  implicit val LocalTimeGetter = new Getter[LocalTime] {
    override def apply(row: Row, ix: Index): Option[LocalTime] = {
      Option(row.getTime(ix(row))).map(_.toLocalTime)
    }
  }

}

trait OffsetDateTimeGetter {
  self: HasOffsetDateTimeFormatter =>

  implicit val OffsetDateTimeGetter = new Parser[OffsetDateTime] {
    override def parse(asString: String): OffsetDateTime = {
      val parsed = offsetDateTimeFormatter.parse(asString)
      OffsetDateTime.from(parsed)
    }
  }

}

trait OffsetTimeGetter {
  self: HasOffsetTimeFormatter =>

  implicit val OffsetTimeGetter = new Parser[OffsetTime] {
    override def parse(asString: String): OffsetTime = {
      val parsed = offsetTimeFormatter.parse(asString)
      OffsetTime.from(parsed)
    }
  }

}

trait BooleanGetter {

  implicit val BooleanGetter: Getter[Boolean] =
    Getter.fromValGetter[Boolean]{ row => ix => row.getBoolean(ix) }

}

trait StringGetter {

  implicit val StringGetter = new Parser[String] {
    override def parse(asString: String): String = asString
  }

}

trait UUIDGetter {

  implicit val UUIDGetter = new Getter[UUID] {
    override def apply(row: Row, ix: Index): Option[UUID] = {
      Option(row.getObject(ix(row))).map {
        case u: UUID =>
          u
        case s: String =>
          UUID.fromString(s)
        case otherwise =>
          throw new SQLException("UUID value expected but not found.")
      }
    }
  }

}

trait InputStreamGetter {

  implicit val InputStreamGetter = new Getter[InputStream] {
    override def apply(row: Row, ix: Index): Option[InputStream] = {
      Option(row.getBinaryStream(ix(row)))
    }
  }

}

trait ReaderGetter {

  implicit val ReaderGetter = new Getter[Reader] {
    override def apply(row: Row, ix: Index): Option[Reader] = {
      Option(row.getCharacterStream(ix(row)))
    }
  }

}

trait AnyRefGetter {

  val AnyRefGetter = new Getter[AnyRef] {
    override def apply(row: Row, ix: Index): Option[AnyRef] = {
      Option(row.getObject(ix(row)))
    }
  }

}
