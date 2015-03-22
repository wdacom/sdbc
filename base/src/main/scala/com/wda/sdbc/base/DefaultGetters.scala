package com.wda.sdbc.base

trait DefaultGetters
  extends AnyRefGetter
  with BooleanGetter
  with ByteGetter
  with BytesGetter
  with DateGetter
  with DoubleGetter
  with FloatGetter
  with InputStreamGetter
  with IntGetter
  with JavaBigDecimalGetter
  with LongGetter
  with ReaderGetter
  with ScalaBigDecimalGetter
  with ShortGetter
  with StringGetter
  with TimeGetter
  with TimestampGetter
  with UUIDGetter {
  self: Row with Getter =>

}

trait Java8DefaultGetters
  extends DefaultGetters
  with InstantGetter
  with LocalDateGetter
  with LocalDateTimeGetter
  with LocalTimeGetter {
  self: Row with Getter =>

}
