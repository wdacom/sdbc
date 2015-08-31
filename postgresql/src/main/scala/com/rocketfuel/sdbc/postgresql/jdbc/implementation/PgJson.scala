package com.rocketfuel.sdbc.postgresql.jdbc.implementation

import org.json4s.jackson.JsonMethods
import org.json4s.{DefaultFormats, Formats, JValue}
import org.postgresql.util.PGobject

class PgJson() extends PGobject() {

  private var formats: Formats = DefaultFormats

  private var value: Option[JValue] = None

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

object PgJson {
  def apply(j: JValue)(implicit formats: Formats = DefaultFormats): PgJson = {
    val p = new PgJson()

    p.value = Some(j)
    p.formats = formats

    p
  }
}
