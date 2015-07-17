name := "jdbc"

publishArtifact in Test := true

libraryDependencies ++= Seq(
  //Connection pooling
  "com.zaxxer" % "HikariCP" % "2.3.9"
)
