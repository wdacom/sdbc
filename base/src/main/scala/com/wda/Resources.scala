package com.wda

import scala.io.Source

/**
 * The purpose of this trait is to be able to easily read class specific resource files.
 * For example, if your class is x.y.Z, you can create the file resources/queries/x/y/Z/file.sql, and then
 * read it with readResource("queries", "file.sql").
 */
trait Resources {
  /**
   * Represents the class name as a path. Package and module specific parts are removed.
   * TODO: Make sure this works inner-objects and so on.
   */
  lazy val classAsPath = {
    val packageSuffix = ".package$"
    val moduleSuffix = "$"

    val clazz = this.getClass.getName.replace('.', '/')

    if (clazz.endsWith(packageSuffix)) {
      clazz.dropRight(packageSuffix.length)
    } else if (clazz.endsWith(moduleSuffix)) {
      clazz.dropRight(moduleSuffix.length)
    } else clazz
  }

  def readResource(resourceType: String, resourceName: String): String = {
    val resourceURL = getClass.getResource(Seq(resourceType, classAsPath, resourceName).mkString("/", "/", ""))
    val source = Source.fromURL(resourceURL)
    try {
      source.mkString
    } finally {
      source.close()
    }
  }

}
