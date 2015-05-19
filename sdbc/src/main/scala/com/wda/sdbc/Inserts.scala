package com.wda.sdbc

trait Inserts[T, Context] {
  def insert(t: T)(implicit context: Context): T
}

trait Updates[T, Context] {
  def update(t: T)(implicit context: Context): T
}

trait Upserts[T, Context] {
  def upsert(t: T)(implicit context: Context): T
}

trait Selects[Id, T, Context] {
  def select(id: Id)(implicit context: Context): T
}
