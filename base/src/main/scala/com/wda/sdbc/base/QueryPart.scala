package com.wda.sdbc.base

private[base] sealed trait QueryPart

private[base] case class Parameter(value: String) extends QueryPart

private[base] case class QueryText(value: String) extends QueryPart
