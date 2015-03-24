package com.wda

object CaseInsensitiveOrdering extends Ordering[String] {
  override def compare(
    x: String,
    y: String
  ): Int = {
    String.CASE_INSENSITIVE_ORDER.compare(x, y)
  }
}
