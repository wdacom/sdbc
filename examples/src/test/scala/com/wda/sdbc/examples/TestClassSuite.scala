package com.wda.sdbc.examples

import com.wda.sdbc.h2.jdbc.H2Suite
import com.wda.sdbc.jdbc.H2._

class TestClassSuite
 extends H2Suite {

  test("insert and select works") { implicit connection =>
    connection.execute("CREATE TEMPORARY TABLE test_class (id int IDENTITY(1,1) PRIMARY KEY, value varchar(100) NOT NULL)")
    assertResult(1)(update(TestClass.Value("hi")))
    val rows = iterator(TestClass.All()).toSeq
    assert(rows.exists(_.value == "hi"))
  }

}
