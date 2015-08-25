organization := "com.rocketfuel.sdbc.jdbc"

name := "base"

publishArtifact in Test := true

libraryDependencies ++= Seq(
  //Connection pooling
  "com.zaxxer" % "HikariCP" % "2.4.0",
  "org.scodec" %% "scodec-bits" % "1.0.9"
)
