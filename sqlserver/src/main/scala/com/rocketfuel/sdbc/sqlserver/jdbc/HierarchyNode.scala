package com.rocketfuel.sdbc.sqlserver.jdbc

case class HierarchyNode(start: Int, path: Int*) {
  override def toString: String = {
    (start +: path).mkString(".")
  }
}

object HierarchyNode {
  def fromString(path: String): HierarchyNode = {
    val pathParts = path.split('.').map(_.toInt)
    apply(pathParts.head, pathParts.tail: _*)
  }
}

trait HierarchyNodeImplicits {
  implicit def IntToHierarchyNode(i: Int): HierarchyNode = {
    HierarchyNode(i)
  }

  implicit def SeqIntToHierarchyNode(path: Seq[Int]): HierarchyNode = {
    HierarchyNode(path.head, path.tail: _*)
  }

  implicit def StringToHierarchyNode(s: String): HierarchyNode = {
    HierarchyNode.fromString(s)
  }
}
