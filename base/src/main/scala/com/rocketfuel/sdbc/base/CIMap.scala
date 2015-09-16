package com.rocketfuel.sdbc.base

import scala.collection.immutable.TreeMap

/**
 * Case insensitive map.
 */
object CIMap {

  implicit val ordering: Ordering[String] = CaseInsensitiveOrdering

  def empty[B] = TreeMap.empty[String, B]

  def apply[B](elems: (String, B)*): TreeMap[String, B] = {
    TreeMap(elems: _*)
  }

}
