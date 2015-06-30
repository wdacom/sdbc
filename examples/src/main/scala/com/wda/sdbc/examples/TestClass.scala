package com.wda.sdbc.examples

import java.sql.Connection

import com.wda.sdbc.H2._

case class TestClass(
  id: Int,
  value: String
)

object TestClass
  extends SelectableMethods[TestClass]
  with UpdatableMethods
  with ExecutableMethods {

  implicit def apply(row: Row): TestClass = {
    TestClass(row[Int]("id").get, row[String]("value").get)
  }

  implicit val selectableByValue = new Selectable[Value, TestClass] {
    val query = Select[TestClass]("SELECT * FROM test_class WHERE value = $value")

    override def select(key: Value)(implicit connection: Connection): Iterator[TestClass] = {
      query.on("value" -> key.value).iterator()
    }
  }

  implicit val selectableById = new Selectable[Id, TestClass] {
    val query = Select[TestClass]("SELECT * FROM test_class WHERE id = $id")

    override def select(key: Id)(implicit connection: Connection): Iterator[TestClass] = {
      query.on("id" -> key.id).iterator()
    }
  }

  implicit val selectableAll = new Selectable[Unit, TestClass] {
    val query = Select[TestClass]("SELECT * FROM test_class")

    override def select(key: Unit)(implicit connection: Connection): Iterator[TestClass] = {
      query.iterator()
    }
  }

  implicit val insertById = new Updatable[Value] {
    val query = Update("INSERT INTO test_class (value) VALUES ($value)")

    override def update(key: Value)(implicit connection: Connection): Long = {
      query.on("value" -> key.value).update()
    }
  }

  case class Value(value: String)

  case class Id(id: Int)

}
