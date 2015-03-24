name := "sdbc"

description := "SDBC is a database API for Scala."

libraryDependencies ++= Seq(
  //Logging
  "org.slf4j" % "slf4j-api" % "1.7.10",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.2" % "test",
  "org.apache.logging.log4j" % "log4j-api" % "2.2" % "test",
  "org.apache.logging.log4j" % "log4j-core" % "2.2" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  //Connection pooling
  "com.zaxxer" % "HikariCP" % "2.3.5",
  //Config file loading
  //https://github.com/typesafehub/config
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.0" % "test",
  "org.apache.commons" % "commons-lang3" % "3.3.2" % "test"
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

testOptions in Test += Tests.Argument("-server")
