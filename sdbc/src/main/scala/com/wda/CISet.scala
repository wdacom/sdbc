package com.wda

import scala.collection.immutable.TreeSet

/**
 * Case insensitive set.
 */
object CISet {

  implicit val ordering: Ordering[String] = CaseInsensitiveOrdering

  val empty = TreeSet.empty[String]

  def apply[A](elems: String*): TreeSet[String] = {
    TreeSet(elems: _*)
  }

}
