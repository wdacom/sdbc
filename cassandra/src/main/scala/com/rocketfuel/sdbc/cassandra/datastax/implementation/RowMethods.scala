package com.rocketfuel.sdbc.cassandra.datastax.implementation

import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteBuffer

import com.datastax.driver.core.{Row => CRow, UDTValue, DataType, TupleValue}

trait RowMethods {
  self: ParameterValues =>

  implicit class Row(underlying: CRow) {

    def get[T](ix: Index)(implicit getter: RowGetter[T]): Option[T] = {
      getter(underlying, ix)
    }

    def parameter(ix: Index): Option[ParameterValue[_]] = {
      Option(underlying.getObject(ix(underlying))).flatMap {
        case map: java.util.Map[_, _] =>
          //The drive returns NULL maps as empty maps rather than NULL.
          //This means that the result is ambiguous. We'll use None,
          //since that's what we'd expect when pattern matching.
          if (map.isEmpty) None
          else Some(JavaMapToParameter(map))
        case list: java.util.List[_] =>
          //Same semantics as for Map
          if (list.isEmpty) None
          else Some(JavaListToParameter(list))
        case set: java.util.Set[_] =>
          //Same semantics as for Map
          if (set.isEmpty) None
          else Some(JavaSetToParameter(set))
        case l: java.lang.Long =>
          Some(LongToParameter(l.longValue()))
        case b: ByteBuffer =>
          Some(ArrayByteToParameter(b.array()))
        case b: java.lang.Boolean =>
          Some(BooleanToParameter(b.booleanValue()))
        case d: java.math.BigDecimal =>
          Some(JavaBigDecimalToParameter(d))
        case d: java.lang.Double =>
          Some(DoubleToParameter(d.doubleValue()))
        case f: java.lang.Float =>
          Some(FloatToParameter(f.floatValue()))
        case i: InetAddress =>
          Some(InetAddressToParameter(i))
        case i: java.lang.Integer =>
          Some(IntToParameter(i.intValue()))
        case s: String =>
          Some(StringToParameter(s))
        case d: java.util.Date =>
          Some(DateToParameter(d))
        case u: java.util.UUID =>
          Some(UUIDToParameter(u))
        case b: BigInteger =>
          Some(BigDecimalToParameter(b))
        case u: UDTValue =>
          Some(UDTValueToParameter(u))
        case t: TupleValue =>
          ???
      }


    }

  }

}
