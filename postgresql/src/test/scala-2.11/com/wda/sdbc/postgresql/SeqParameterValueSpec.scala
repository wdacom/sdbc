package com.wda.sdbc.postgresql

import org.scalatest.BeforeAndAfterAll

import scalaz.Scalaz._

class SeqParameterValueSpec
  extends PostgreSqlSuite
  with BeforeAndAfterAll {
  testSelect[Seq[Option[Boolean]]]("SELECT '{}'::boolean[]", Seq.empty[Option[Boolean]].some)

  testSelect[Seq[Boolean]]("SELECT '{t}'::boolean[]", Seq(true).some)

  testSelect[Seq[LTree]]("SELECT ARRAY['l.tree']::ltree[]", Seq(LTree("l", "tree")).some)

  testSelect[Seq[Int]]("SELECT '{1,2,3}'::int[]", SeqParameterValueSpec.oneTwoThree.some)

  testSelect[Seq[Seq[Int]]]("SELECT '{{1},{2},{3}}'::int[]", SeqParameterValueSpec.oneTwoThree.map(x => Seq(x)).some)

  testSelect[Seq[Seq[Int]]]("SELECT '{{1,2,3},{4,5,6}}'::int[][]", Seq(SeqParameterValueSpec.oneTwoThree, SeqParameterValueSpec.fourFiveSix).some)

  override protected def beforeAll(): Unit = {
    pgBeforeAll()
    createLTree()
  }

  override protected def afterAll(): Unit = {
    pgAfterAll()
  }
}

object SeqParameterValueSpec {
  val oneTwoThree = Seq(1, 2, 3)

  val fourFiveSix = Seq(4, 5, 6)
}