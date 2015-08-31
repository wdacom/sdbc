package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import java.sql.SQLException

import org.json4s.jackson.JsonMethods
import org.json4s.{DefaultFormats, Formats, JValue}
import org.postgresql.util.PGobject

class PGJson() extends PGobject() {

  private var formats: Formats = DefaultFormats

  var value: Option[JValue] = None

  override def getValue: String = {
    value.map(j => JsonMethods.compact(JsonMethods.render(j))).orNull
  }

  override def setValue(value: String): Unit = {
    this.value = for {
      reallyValue <- Option(value)
    } yield {
        //PostgreSQL uses numeric (i.e. BigDecimal) for json numbers
        //http://www.postgresql.org/docs/9.4/static/datatype-json.html
        JsonMethods.parse(reallyValue, useBigDecimalForDouble = true)
      }
  }

}

object PGJson {
  def apply(j: JValue)(implicit formats: Formats = DefaultFormats): PGJson = {
    val p = new PGJson()

    p.value = Some(j)
    p.formats = formats

    p
  }
}

trait PGJsonImplicits {

  implicit def JValueToPGJson(j: JValue)(implicit formats: Formats = DefaultFormats): PGJson = {
    PGJson(j)
  }

  implicit def PGobjectToJValue(x: PGobject): JValue = {
    x match {
      case p: PGJson =>
        p.value.get
      case _ =>
        throw new SQLException("column does not contain a json")
    }
  }

}
