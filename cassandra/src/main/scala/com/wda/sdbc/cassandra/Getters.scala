package com.wda.sdbc.cassandra

import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.{UUID, Date}
import scala.collection.convert.wrapAsScala._
import com.datastax.driver.core.{Row => CRow, UDTValue, TupleValue, Token}
import com.google.common.reflect.TypeToken

trait Getters {
  implicit val BooleanGetter: Getter[Boolean] = Getters[Boolean](row => ix => row.getBool(ix))

  implicit val BoxedBooleanGetter: Getter[java.lang.Boolean] = Getters[java.lang.Boolean](row => ix => row.getBool(ix))

  implicit val BytesGetter: Getter[ByteBuffer] = Getters[ByteBuffer](row => ix => row.getBytes(ix))

  implicit val DateGetter: Getter[Date] = Getters[Date](row => ix => row.getDate(ix))

  implicit val BigDecimalGetter: Getter[BigDecimal] = Getters[BigDecimal](row => ix => row.getDecimal(ix))

  implicit val JavaBigDecimalGetter: Getter[java.math.BigDecimal] = Getters[java.math.BigDecimal](row => ix => row.getDecimal(ix))

  implicit val IntGetter: Getter[Int] = Getters[Int](row => ix => row.getInt(ix))

  implicit val BoxedIntGetter: Getter[java.lang.Integer] = Getters[java.lang.Integer](row => ix => row.getInt(ix))

  implicit val LongGetter: Getter[Long] = Getters[Long](row => ix => row.getLong(ix))

  implicit val BoxedLongGetter: Getter[java.lang.Long] = Getters[java.lang.Long](row => ix => row.getLong(ix))

  implicit val FloatGetter: Getter[Float] = Getters[Float](row => ix => row.getFloat(ix))

  implicit val BoxedFloatGetter: Getter[java.lang.Float] = Getters[java.lang.Float](row => ix => row.getFloat(ix))

  implicit val DoubleGetter: Getter[Double] = Getters[Double](row => ix => row.getDouble(ix))

  implicit val BoxedDoubleGetter: Getter[java.lang.Double] = Getters[java.lang.Double](row => ix => row.getDouble(ix))

  implicit val InetGetter: Getter[InetAddress] = Getters[InetAddress](row => ix => row.getInet(ix))

  implicit val StringGetter: Getter[String] = Getters[String](row => ix => row.getString(ix))

  implicit val TokenGetter: Getter[Token] = Getters[Token](row => ix => row.getToken(ix))

  implicit val UUIDGetter: Getter[UUID] = Getters[UUID](row => ix => row.getUUID(ix))

  implicit val TupleValueGetter: Getter[TupleValue] = Getters[TupleValue](row => ix => row.getTupleValue(ix))

  implicit val UDTValueGetter: Getter[UDTValue] = Getters[UDTValue](row => ix => row.getUDTValue(ix))

  implicit def SeqGetter[T]: Getter[Seq[T]] = Getters[Seq[T]](row => ix => row.getList[T](ix, new TypeToken[T]() {}))

  implicit def JavaListGetter[T]: Getter[java.util.List[T]] = Getters[java.util.List[T]](row => ix => row.getList[T](ix, new TypeToken[T]() {}))

  implicit def SetGetter[T]: Getter[Set[T]] = Getters[Set[T]](row => ix => row.getSet[T](ix, new TypeToken[T]() {}).toSet)

  implicit def JavaSetGetter[T]: Getter[java.util.Set[T]] = Getters[java.util.Set[T]](row => ix => row.getSet[T](ix, new TypeToken[T]() {}))

  implicit def MapGetter[K, V]: Getter[Map[K, V]] = Getters[Map[K, V]](row => ix => row.getMap[K, V](ix, new TypeToken[K] {}, new TypeToken[V] {}).toMap)

  implicit def JavaMapGetter[K, V]: Getter[java.util.Map[K, V]] = Getters[java.util.Map[K, V]](row => ix => row.getMap[K, V](ix, new TypeToken[K] {}, new TypeToken[V] {}))

  val AnyRefGetter: Getter[AnyRef] = Getters[AnyRef](row => ix => row.getObject(ix))
}

object Getters {
  def apply[T](getter: CRow => Int => T): Getter[T] = {
    new Getter[T] {
      override def apply(row: CRow, toIx: Index): Option[T] = {
        val ix = toIx(row)
        if (row.isNull(ix)) None
        else Some(getter(row)(toIx(row)))
      }
    }
  }
}
