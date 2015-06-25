name := "sdbc-java7"

description := "SDBC is a database API for Scala."

libraryDependencies ++= Seq(
  //Logging
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.2" % "test",
  "org.apache.logging.log4j" % "log4j-api" % "2.2" % "test",
  "org.apache.logging.log4j" % "log4j-core" % "2.2" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  //Connection pooling
  "com.zaxxer" % "HikariCP-java6" % "2.3.8",
  //Config file loading
  //https://github.com/typesafehub/config
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.2" % "test",
  "org.apache.commons" % "commons-lang3" % "3.4" % "test",
  "joda-time" % "joda-time" % "2.8.1"
)

libraryDependencies <++= scalaVersion { version =>
  val VersionRegex = """(\d+).(\d+)\.?.*""".r("major", "minor")
  lazy val xmlDependency = "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
  val VersionRegex(major, minor) = version
  (major.toInt, minor.toInt) match {
    case (2,minor) if minor < 11 =>
      Vector.empty
    case (2,_) =>
      Vector(xmlDependency)
    case _ =>
      //We're not in version 2, and who knows what
      //xml support is like.
      ???
  }

}
