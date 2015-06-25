package com.wda.sdbc.cassandra

import java.net.InetAddress
import java.nio.ByteBuffer
import java.util.{UUID, Date}
import scala.collection.convert.wrapAsScala._
import com.datastax.driver.core.{Row => CRow, UDTValue, TupleValue, Token}
import com.google.common.reflect.TypeToken

trait Getters {
  implicit val BooleanGetter = Getters[Boolean](row => ix => row.getBool(ix))

  implicit val BytesGetter = Getters[ByteBuffer](row => ix => row.getBytes(ix))

  implicit val DateGetter = Getters[Date](row => ix => row.getDate(ix))

  implicit val BigDecimalGetter = Getters[BigDecimal](row => ix => row.getDecimal(ix))

  implicit val JavaBigDecimalGetter = Getters[java.math.BigDecimal](row => ix => row.getDecimal(ix))

  implicit val IntGetter = Getters[Int](row => ix => row.getInt(ix))

  implicit val LongGetter = Getters[Long](row => ix => row.getLong(ix))

  implicit val FloatGetter = Getters[Float](row => ix => row.getFloat(ix))

  implicit val DoubleGetter = Getters[Double](row => ix => row.getDouble(ix))

  implicit val InetGetter = Getters[InetAddress](row => ix => row.getInet(ix))

  implicit val StringGetter = Getters[String](row => ix => row.getString(ix))

  implicit val TokenGetter = Getters[Token](row => ix => row.getToken(ix))

  implicit val UUIDGetter = Getters[UUID](row => ix => row.getUUID(ix))

  implicit val TupleValueGetter = Getters[TupleValue](row => ix => row.getTupleValue(ix))

  implicit val UDTValueGetter = Getters[UDTValue](row => ix => row.getUDTValue(ix))

  implicit def SeqGetter[T] = Getters[Seq[T]](row => ix => row.getList[T](ix, new TypeToken[T]() {}))

  implicit def JavaListGetter[T] = Getters[java.util.List[T]](row => ix => row.getList[T](ix, new TypeToken[T]() {}))

  implicit def SetGetter[T] = Getters[Set[T]](row => ix => row.getSet[T](ix, new TypeToken[T]() {}).toSet)

  implicit def JavaSetGetter[T] = Getters[java.util.Set[T]](row => ix => row.getSet[T](ix, new TypeToken[T]() {}))

  implicit def MapGetter[K, V] = Getters[Map[K, V]](row => ix => row.getMap[K, V](ix, new TypeToken[K] {}, new TypeToken[V] {}).toMap)

  implicit def JavaMapGetter[K, V] = Getters[java.util.Map[K, V]](row => ix => row.getMap[K, V](ix, new TypeToken[K] {}, new TypeToken[V] {}))

  val AnyRefGetter = Getters[AnyRef](row => ix => row.getObject(ix))
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
