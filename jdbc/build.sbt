organization := "com.wda.sdbc.jdbc"

name := "base"

publishArtifact in Test := true

libraryDependencies ++= Seq(
  //Connection pooling
  "com.zaxxer" % "HikariCP" % "2.4.0"
)
