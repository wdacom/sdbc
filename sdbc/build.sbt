name := "sql"

description := "SDBC is a database API for Scala."

libraryDependencies ++= Seq(
  //Logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.2" % "test",
  "org.apache.logging.log4j" % "log4j-api" % "2.2" % "test",
  "org.apache.logging.log4j" % "log4j-core" % "2.2" % "test",
  //Connection pooling
  "com.zaxxer" % "HikariCP" % "2.3.5",
  //Config file loading
  //https://github.com/typesafehub/config
  "com.typesafe" % "config" % "1.2.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.0" % "test",
  "org.apache.commons" % "commons-lang3" % "3.3.2" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.3"
)

testOptions in Test += Tests.Argument("-server")
